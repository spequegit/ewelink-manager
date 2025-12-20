const ewelink = require('ewelink-api');

(async () => {

  const connection = new ewelink({
    email: '<your ewelink email>',
    password: '<your ewelink password>',
    region: '<your ewelink region>',
  });

  /* get all devices */
  const devices = await connection.getDevices();
  console.log(devices);

  /* get specific devide info */
  const device = await connection.getDevice('<your device id>');
  console.log(device);

  /* toggle device */
  await connection.toggleDevice('<your device id>');

})();