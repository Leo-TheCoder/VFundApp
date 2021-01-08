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

router.post('/createuser', (req, res) => {
    var email = req.body.email;
    var password = req.body.password
    var username = req.body.username
    var avatar = req.body.avatar;

    var user = { StringPrefix: 'US', Username: username, UserPassword: password, UserEmail: email, isAdmin: false,avatar: avatar }

    var transaction = new sql.Transaction(conn);
    transaction.begin().then(function () {
        conn.query(`INSERT INTO UserAccount(StringPrefix,Username,UserPassword) 
        VALUES ('${user.ID}','${user.Username}','${user.UserPassword}')`)
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