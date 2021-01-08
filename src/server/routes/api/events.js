const express = require('express');
var serviceAccount = require("../../server-key.json");
const sql = require("mssql/msnodesqlv8");

var router = express.Router();

const conn = new sql.ConnectionPool({
    database: "ServerDB",
    server: "localhost",
    driver: "msnodesqlv8",
    options: {
        trustedConnection: true,
        enableArithAbort: true
    },
    port: 8883
});

// connect to database
conn.connect().then(() => {
    console.log('Connected!');
});

/* GET home page. */
router.get('/', function (req, res) {
    res.render('index', { title: 'Express' });
});

//query table UserAccount
router.get('/getevent', function (req, res) {
    var queryString = `select * from Event`;
    var second = false;

    for (const key in req.query) {
        console.log(key, req.query[key])
        if (!second) {
            queryString += `where ${key} = '${req.query[key]}'`;
            second = true;
        }
        else
            queryString += `AND ${key} = '${req.query[key]}'`;
    }

    conn.query(queryString)
        .then(result => {
            res.send(result);
        }).catch(err => {
            // ... error checks
            if (err) {
                console.error('Error !!!');
                throw err
            }
        });
});

router.post('/createevent', (req, res) => {
    var EventName = req.body.EventName,
        EventDescription = req.body.EventDescription,
     EventDate = req.body.EventDate,
     EventGoal = req.body.EventGoal,
     EventRate = req.body.EventRate;

    var event = {
        StringPrefix: 'E', EventName: EventName,
        EventDescription: EventDescription,
        EventDate: EventDate,
        EventGoal: EventGoal,
        EventRate: EventRate
    }

    var transaction = new sql.Transaction(conn);
    transaction.begin().then(function () {
        conn.query(`INSERT INTO Event(StringPrefix,EventName,EventDescription,EventDate,EventGoal,EventRate) 
        VALUES ('${event.ID}','${event.EventName}','${event.EventDescription}','${event.EventDate}','${event.EventGoal}','${event.EventRate}')`)
            .then(function () {
                transaction.commit().then(function (recordSet) {
                    console.log(recordSet);
                    conn.close();
                    return res.send('Inserted successfully');
                }).catch(function (err) {
                    console.log("Error in Transaction Commit " + err);
                    conn.close();
                    return res.send('Error');
                });
            });
    });
});

module.exports = router;