import fs from 'fs-extra';

const outputDir = './output';
const outputFilePath = './output/output.json';

/**
 * Writes the patients output JSON to file
 * @param  {array} patients array of Patient objects
 */
export function writeJsonOutput(patients) {
    fs.emptyDirSync(outputDir);
    const outputObj = { patients: [patients] };
    fs.writeJsonSync(outputFilePath, outputObj, {spaces: 2, replacer: jsonReplacer});
}

/**
 * Replacer to be passed to writer. Prevents patientIdentifier from being written in output
 * @param  {string} key   the JSON key
 * @param  {object} value the JSON property
 * @return {object}       the JSON property to be written
 */
function jsonReplacer(key, value) {
    if (key === 'patientIdentifier') {
        return undefined;
    }

    return value;
}
