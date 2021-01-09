const express = require('express');
var serviceAccount = require("../../server-key.json");
const sql = require("mssql/msnodesqlv8");

var jsonParser = express.json();

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


router.param('eventId', function (req, res, next, id) {
    console.log(id);
    next()
  })


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
    queryString += ` LEFT JOIN UserAccount ON UserAccount.IDandPrefix = Event.HostID`;

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

router.get('/getfollowusers/:eventId', function (req, res) {
    const eventId = req.params.eventId;
    var queryString = `select * from Follow INNER JOIN UserAccount ON UserAccount.IDandPrefix = Follow.UserID
    WHERE Follow.EventID = '${eventId}';`

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
    var queryString = "INSERT INTO Event ";
    var cols,values;
    cols="(";
    values ="(";
    var second = false;

    for (const [key, value] of Object.entries(req.body)) {
        if(!second){
            cols += `${key}`;
            values += `${value}`;
            second=true;
        }
        else{
            cols += `,${key}`;
            values += `,${value}`;
        } 

    }
    cols+=')';
    values+=')';

    queryString+=cols+" VALUES "+values;
    console.log(queryString)

    var transaction = new sql.Transaction(conn);
    transaction.begin().then(function () {
        conn.query(queryString)
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


router.patch('/patchevent/:eventId', jsonParser, (req, res) => {
    const eventId = req.params.eventId;

    var queryString = "UPDATE Event ";
    var second = false;

    for (const [key, value] of Object.entries(req.body)) {
        if (!second) {
            queryString += `SET ${key} = '${value}'`;
            second = true;
        }
        else
            queryString += `, ${key} = '${value}'`;
    }
    queryString+=" WHERE IDandPrefix = '" + eventId + "';"
    console.log(queryString)

    conn.query(queryString, function(err,data){
        var result = {};
        if (err) {
            conn.release();
        } else {
            conn.release();
            result = data;
        }
    });
    res.end()
});

module.exports = router;