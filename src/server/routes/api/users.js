const express = require('express');
var serviceAccount = require("../../server-key.json");
const sql = require("mssql/msnodesqlv8");
const { QueryEvent } = require('msnodesqlv8');

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

// connect to database
conn.connect().then(() => {
    console.log('Connected!');
});

/* GET home page. */
router.get('/', function (req, res) {
    res.render('index', { title: 'Express' });
});

router.param('userId', function (req, res, next, id) {
    console.log(id);
    next()
  })

//query table UserAccount
router.get('/getuser', function (req, res) {
    var queryString = `select * from UserAccount `;
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

//get followed event
router.get('/getfollowevents/:userId', function (req, res) {
    const userId = req.params.userId;
    var queryString = `select * from Follow INNER JOIN Event ON Event.IDandPrefix = Follow.EventID
    WHERE Follow.UserID = '${userId}';`

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


router.post('/createuser', jsonParser, (req, res) => {
    console.log(req.body);
    var queryString = "INSERT INTO UserAccount ";
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
                    return res.send('Error')
                });
            });
    });
});

router.patch('/patchuser/:userId', jsonParser, (req, res) => {
    const userId = req.params.userId;

    var queryString = "UPDATE UserAccount ";
    var second = false;

    for (const [key, value] of Object.entries(req.body)) {
        if (!second) {
            queryString += `SET ${key} = '${value}'`;
            second = true;
        }
        else
            queryString += `, ${key} = '${value}'`;
    }
    queryString+=" WHERE IDandPrefix = '" + userId + "';"
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