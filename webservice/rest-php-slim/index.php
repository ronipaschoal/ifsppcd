<?php 

require_once("vendor/autoload.php");
require_once("controller/SensorController.php");
require_once("controller/TipoMedicaoController.php");
require_once("controller/MedicoesController.php");
require_once("controller/EstacaoPcdController.php");
require_once("controller/SensorInstaladoController.php");

use \Slim\Slim;

\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();
$app->response()->header('Content-Type', 'application/json;charset=utf-8');

// Rotas do cadastro da entidade SENSOR
$app->get('/pcd/sensores', 'getAllSensores');
$app->get('/pcd/sensor/:id', 'getSensor');
$app->post('/pcd/sensor', 'setSensor');
$app->put('/pcd/sensor/:id', 'updateSensor');
$app->delete('/pcd/sensor/:id', 'deleteSensor');

// Rotas do cadastro da entidade TIPO_MEDICAO
$app->get('/pcd/tiposmedicao', 'getAllTiposMedicao');
$app->get('/pcd/tipomedicao/:id', 'getTipoMedicao');
$app->post('/pcd/tipomedicao', 'setTipoMedicao');
$app->put('/pcd/tipomedicao/:id', 'updateTipoMedicao');
$app->delete('/pcd/tipomedicao/:id', 'deleteTipoMedicao');

// Rotas do cadastro da entidade MEDICOES
$app->get('/pcd/medicoes', 'getAllMedicoes');
$app->get('/pcd/medicao/:id', 'getMedicao');
$app->post('/pcd/medicao', 'setMedicao');
$app->put('/pcd/medicao/:id', 'updateMedicao');
$app->delete('/pcd/medicao/:id', 'deleteMedicao');

// Rotas do cadastro da entidade ESTACOES_PCD
$app->get('/pcd/estacoes', 'getAllEstacoes');
$app->get('/pcd/estacao/:id', 'getEstacao');
$app->post('/pcd/estacao', 'setEstacao');
$app->put('/pcd/estacao/:id', 'updateEstacao');
$app->delete('/pcd/estacao/:id', 'deleteEstacao');

// Rotas do cadastro da entidade SENSOR
$app->get('/pcd/sensoresinstalados', 'getAllSensoresInstalados');
$app->get('/pcd/sensorinstalado/:id', 'getSensorInstalado');
$app->post('/pcd/sensorinstalado', 'setSensorInstalado');
$app->put('/pcd/sensorinstalado/:id', 'updateSensorInstalado');
$app->delete('/pcd/sensorinstalado/:id', 'deleteSensorInstalado');

// Inicia a aplicação Slim
$app->run();

// Conexão com o banco de dados
function getConn()
{
 return new PDO('mysql:host=localhost;dbname=ESTACAO_METEREOLOGICA',
  'root',
  '1234',
  array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8")
  
  );
}



?>
