//$env:GOOGLE_APPLICATION_CREDENTIALS=".\server-key.json"
const express = require('express');
const bodyParser = require('body-parser');


//init express
const app = express();

//create dynamic port
const port = process.env.PORT || 8080;

//create Routes
app.use('/api/users', require('./routes/api/users'));
app.use('/api/events', require('./routes/api/events'));

app.use(express.json());
app.use(bodyParser.urlencoded({
    extended: true
  }));
  
app.get('/', (req, res) => {
    res.send('Hello from server');
})

//Listen 
app.listen(port, () => console.log('Server listenning on Port ' + port));