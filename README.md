# HealthForge Coding Quiz

# Solution
For Questions 1 and 2 see tests and swagger-ui at [http://localhost:8080/](http://localhost:8080/)

For Question 3, see output.json in the output directory of csv-results-parser. To run code, ensure npm and node are installed then, from the csv-results-parser directory, run:
```
$ npm install

$ npm start
```

# Introduction
Hello! Thanks for taking the time to do our technical test. We know these are annoying, but we need a way to assess everyone that we employ to avoid disappointment on both sides later on.

Please follow the instructions below and when you are done, put your code into a git repo you own and send us a link to it for us to assess. GitHub or BitBucket are both free for public repos.
 
Feel free to use as much Google-Fu as you need to (that is what we do on a daily basis) - however, you need to do the test on your own. Any outside help is not in the spirit of the test and will result in seven or eight years of bad luck, depending on which is worst for you.

**Impress us with your skills and show us what you can do! This is your chance to shine and the more we see the better.**

# Pre-requesites

You will need:

- a git client
- Java 8
- Maven 3.3 or later
- an IDE of your choice

Things you should search for on the web if you don't know about them already are:

- Spring Boot, Spring MVC
- FasterXML's Jackson for JSON support
- JodaTime for dates
- JUnit
- Swagger

We haven't tried this on Windows, but it should work and it definitely works on MacOS or Linux.

# Getting Started

Clone the repo somewhere locally.

The source code will initially not build, but that is part of the test... so don't worry about that just yet.

The code implements a REST API for managing patients, clinicians and orders. It has documentation provided by Swagger that allows you to play with the API by going to [http://localhost:8080/](http://localhost:8080/) once the code compiles and runs.

Later on in the test, when you need to run the app, you can do this with:

    mvn spring-boot:run
    
And to run integration tests:

    mvn test

# Question 1

Currently the patient API only allows searching by date of birth.

**We would like you to add a new feature to the Patient API's `getAll` method that
 allows searching by `postcode`. The user would send a request as follows to search by postcode:**
 
    http://localhost:8080/api/patient?postcode=90210

And the expected response would be:

    {
      "totalItems": 1,
      "items": [
        {
          "id": "...",
          "firstName": "Patient",
          "lastName": "Epsilon",
          ...
        }
      ]
    }

#### Addresses

The addresses in our patient dataset have the following formats:

    Sumo Informática S.A.
    Calle 39 No 1540
    B1000TBU San Sebastian       Postal code, Municipality
    Argentina

    LANCE MULTILINGUA-EBEL
    FOREIGN TEACHERS' MAILBOX
    LIANGANG MIDDLE SCHOOL
    LOUDI
    HUNAN
    417009                        Postal code
    People's Republic of China

    вул. Цэнтральная, д. 20       Streetname, number, apartment/room
    в. Караліставічы              Village (in rural areas when different from post office)
    223016, п/а Новы Двор         Postal code, post office (in rural areas) or city/town
    Мінскага р-на.                Raion
    Мінскай вобл.                 Region
    Belarus                       Country

    日本国 〒112-0001              Country, 〒Postal Code
    東京都文京区白山4丁目3-2         Prefecture, City, District, Chōme, Banchi, Building Number and Name, Room Number
    Japan
                        
    90210 Palm Drive              Number and street name
    Greenville                    Town
    CA 90210                      State Code, Postal Code
    USA                           Country

#### Some tips

- There is an integration test in the code already, that will test if you got it right...
- We like object oriented programming and we hope that you do too!
- We like unit tests and integration tests
- This is a Spring project, so EJBs would be a bit out of place, as would custom singletons
- The patients will have random UUIDs each time you run the app, as they are loaded from file at startup

And finally: 

- The code must build and function correctly, even after your additions, don't mess with things outside of the Patient API, unless it adds value
- Someone has to look at the code you add, so help them out, just like you would day-to-day
    
    
# Question 2

**Add a new API that handles Appointments. The base URL for appointments should be:**

    http://localhost:8080/api/appointments
    
It should have the following methods:

    GET /api/appointments                          => list all appointments with paging
    GET /api/appointments?patientId=<uuid>         => list all appointments for a patient by their id
    GET /api/appointments/{appointmentId}          => get an appointment by appointment id
    POST /api/appointments                         => add an appointment
    PUT /api/appointments/{appointmentId}          => update an appointment
    DELETE /api/appointments/{appointmentId}       => remove an appointment
    
Appointments should have the following data structure:
    
    {
        id : UUID               => appointment id
        status : enum           => a status code: NEW, PENDING, IN_PROGRESS, COMPLETE, CANCEL, CANCELLED, DNA (did not attend)
        startsAt : DateTime     => start date and time, with timezone
        endAt : DateTime        => end date and time, with timezone
        patientId : UUID        => the patient the appointment is for
        clinicianId : UUID      => the clinician the appointment is with
        reason : String         => reason for the appointment
     }   
    
Sometimes patients do not attend appointments and they need to be marked as `DNA` for later analysis.

Other requirements for the API are:

- it must read and write JSON
- it should be thread-safe (the first user updating a record will 'win' and have their changes overwritten by the second user)
- list results should be paged
- appointments progress through various states: they start in the NEW state and then progress through PENDING (before the appointment starts) to IN_PROGRESS (on attendance) to COMPLETE (once done). Sometimes patients request to CANCEL appointments, which become fully CANCELLED when removed from the roster. And occasionally patients Do Not Attend (DNA).
- there are obvious restrictions on the states that the appointment can transition through (e.g. NEW -> PENDING) and states it would never progress through (e.g. IN_PROGRESS -> DNA)
 
It would be nice to see (but not necessary):

- evidence of many appointments being processed simultaneously
- error reporting when invalid state transitions are attempted
- good object orientation and use of programming patterns

# Question 3

Look in the `data` directory at the lab results for various patients. 

**We would like you to write an application that takes the CSV file and creates 
JSON output of the following format:**

    {
        "patients" : [
            {
                "id" : <uuid>,
                "firstName": <string>,
                "lastName": <string>,
                "dob": <ISO 8601 timestamp>,
                "lab_results" : [
                    {
                        "timestamp": <ISO 8601 timestamp>,
                        "profile" : {
                            "name": <string>,
                            "code": <string>
                        },
                        "panel" : [ 
                            {
                                "code": <string>     SNOMED code of test type
                                "label": <string>    original code of test type
                                "value": <double>    value only
                                "unit": <string>     unit string
                                "lower": <double>    lower range bound
                                "upper": <double>    upper range bound
                            },
                            {
                               ...
                            },
                            {
                               ...
                            }
                        ]
                    },
                    {
                      ...
                    },
                    {
                      ...
                    }
                ]
            },
            {
              ...
            },
            {
              ...
            }
        ]
    }
    
Luckily, the lab results you have been given are more structured, as they have been pre-processed from their original form:

    Test                  Units       Value       Reference Range
    Haemoglobin           g/L         176         135 - 180
    Red Cell Count        x10*12/L    5.9         4.2 - 6.0
    Haematocrit                       0.55+       0.38 - 0.52
    Mean Cell Volume      fL          99+         80 - 98
    Mean Cell Haemoglobin pg          36+         27 - 35
    Platelet Count        x10*9/L     444         150 - 450
    White Cell Count      x10*9/L     4.6         4.0 - 11.0
    Neutrophils           %           20
    Neutrophils           x10*9/L     0.9---      2.0 - 7.5
    Lymphocytes           %           20
    Lymphocytes           x10*9/L     0.9-        1.1 - 4.0
    Monocytes             %           20
    Monocytes             x10*9/L     0.9         0.2 - 1.0
    Eosinophils           %           20
    Eosinophils           x10*9/L     0.92++      0.04 - 0.40
    Basophils             %           20
    Basophils             x10*9/L     0.92+++     <0.21

Below is an example of a panel from `labresults.csv`:
    
    40681648,41860BONALP~55,09/08/2014,BONE PROFILE,BON,ALP~55,ALB~37,CA~2.18,xCCA~2.34,PHOS~1.29,,,,,,,,,,,,,,,,,,,,,ALP,IU/L,35,104
    40681648,41860BONALP~55,09/08/2014,BONE PROFILE,BON,ALP~55,ALB~37,CA~2.18,xCCA~2.34,PHOS~1.29,,,,,,,,,,,,,,,,,,,,,ALB,g/L,34,50
    40681648,41860BONALP~55,09/08/2014,BONE PROFILE,BON,ALP~55,ALB~37,CA~2.18,xCCA~2.34,PHOS~1.29,,,,,,,,,,,,,,,,,,,,,CA,mmol/L,2.2,2.6
    40681648,41860BONALP~55,09/08/2014,BONE PROFILE,BON,ALP~55,ALB~37,CA~2.18,xCCA~2.34,PHOS~1.29,,,,,,,,,,,,,,,,,,,,,xCCA,mmol/L,2.2,2.6
    40681648,41860BONALP~55,09/08/2014,BONE PROFILE,BON,ALP~55,ALB~37,CA~2.18,xCCA~2.34,PHOS~1.29,,,,,,,,,,,,,,,,,,,,,PHOS,mmol/L,0.87,1.45
    
This represents the following lab results:

    Code   Value  Units    Lower  Upper
    -----------------------------------
    ALP    55     IU/L     35     104
    ALB    37     g/L      34     50
    CA     2.18   mmol/L   2.2    2.6
    xCCA   2.34   mmol/L   2.2    2.6
    PHOS   1.29   mmol/L   0.87   1.45
    
The file `labresults-codes.csv` will help you convert the lab's codes into simple SNOMED CT codes, for example:

A code of `HBGL` corresponds to a SNOMED code of `718-7` and indicates the Haemoglobin count in the blood sample.

A 'panel' is a series of tests performed together on a group of samples.

#### Requirements

- use any language you like
- parse the files in any way you think is best
- pretty print the output JSON file and call it `output.json`
- include both your code and the output file in your answer

It would be great if we could build and run all of your code in one go for all the answers to all of your questions.

# And finally...

Good luck and have fun!