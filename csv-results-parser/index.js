import parseSync from 'csv-parse/lib/sync';
import parse from 'csv-parse';
import fs from 'fs-extra';
import moment from 'moment';
import LabResultCode from './lib/model/lab-result-code'
import Patient from './lib/model/patient';
import Panel from './lib/model/panel';
import Profile from './lib/model/profile';
import LabResult from './lib/model/lab-result';
import { writeJsonOutput } from './lib/file-writer';

const labResultCodesPath = 'data/labresults-codes.csv';
const patientsPath = 'data/patients.json';
const labResultsPath = 'data/labresults.csv';

// to hold {key: labResultCode}
const codesLookup = {};

const codeCsvInput = fs.readFileSync(labResultCodesPath).toString();
const codeCsvParsed = parseSync(codeCsvInput);

codeCsvParsed.forEach((row, index) => {
    if (index > 0) {
        const labResultCode = new LabResultCode(...row);
        codesLookup[labResultCode.key] = labResultCode;
    }
});

const patientsJson = fs.readJsonSync(patientsPath)

// to hold {HospID: patient.id}
const patientIdLookup = {};

const patients = [];

patientsJson.forEach((patient) => {
    patients.push(new Patient(patient.id, patient.firstName, patient.lastName, patient.dateOfBirth, []));
    patient.identifiers.forEach((identifier) => {
        patientIdLookup[identifier] = patient.id;
    });
});

// to hold {SampleID: lab_result}
const labResultsLookup = {};

fs.readFile(labResultsPath, (err, buf) => {
    parse(buf.toString(), (err, output) => {
        output.forEach((row, index) => {
            if (index > 0) {
                const label = row[30];
                const labResultCode = codesLookup[label];
                const valueWithLabel = row.find(x => x.startsWith(label+'~'));
                const value = extractValue(valueWithLabel);
                const panel = new Panel(labResultCode.code, label, parseFloat(value), row[31], parseFloat(row[32]), parseFloat(row[33]));

                const sampleId = row[1];
                if (labResultsLookup[sampleId]) {
                    const labResult = labResultsLookup[sampleId];
                    labResult.panel = [panel, ...labResult.panel];
                } else {
                    const profile = new Profile(row[3], row[4]);
                    const patientIdentifier = row[0];
                    const timestamp = moment(row[2], 'DD/MM/YYYY').format('YYYY-MM-DD');
                    const labResult = new LabResult(patientIdentifier, timestamp, profile, [panel]);
                    labResultsLookup[sampleId] = labResult;
                }
            }
        });

        for (const result of Object.values(labResultsLookup)) {
            const patientId = patientIdLookup[result.patientIdentifier];
            const patient = patients.find(patient => patient.id === patientId);
            patient.lab_results = [result, ...patient.lab_results];
        }

        writeJsonOutput(patients);
    });
});

/**
 * Extracts the value from a label with attached value (e.g. ABC~12.3)
 * @param  {string} valueWithLabel the label and value delimited by ~
 * @return {string}                the value
 */
function extractValue(valueWithLabel) {
    if (valueWithLabel.includes('>')) {
        return valueWithLabel.split('~>')[1];
    }

    if (valueWithLabel.includes('<')) {
        return valueWithLabel.split('~<')[1];
    }

    if (valueWithLabel.includes('**')) {
            return valueWithLabel.split('~**')[1];
    }

    return valueWithLabel.split('~')[1]
}
