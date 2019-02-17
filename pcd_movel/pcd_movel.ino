#include <Wire.h>
#include <Adafruit_BMP085.h>
#include <DallasTemperature.h>
#include <OneWire.h>
#include <WiFi.h>
#include <HTTPClient.h>

#define MQa 12 //Sensor Gás
#define MQd 13 //Sensor Gás
#define YL 2 //Sensor Chuva
#define ML 14 //Sensor UV
#define DS 15 //Sensor Temperatura Externo

OneWire oneWire (DS);
DallasTemperature sensors(&oneWire);

Adafruit_BMP085 bmp;

const char* ssid = "PCDs";
const char* password = "senha123";
WiFiServer server(80);

bool sendData = false;

 void sendHttpRequest(String cod, String value, bool sendData) {

  if(sendData == true) {
     
    HTTPClient http;
    http.setReuse(true);
    http.begin("http://192.168.1.100/webservice/TempoReal/index.php");
    http.addHeader("Content-Type", "application/json");
    String json = "{\"leitura\":{\"codigo\":\"" + cod + "\",\"valor\":\"" + value + "\"}}";

    int httpCode = http.POST(json);
    
    Serial.println(json);
    http.end();
      
    }
  } 

void setup() {

  Serial.begin(9600);
  //Serial.begin(115200);
  delay(4000);
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Conectando WiFi..");
  }

  Serial.println("Conectado ao WiFi ");
  Serial.println(WiFi.localIP());
  server.begin();
   
  pinMode(MQa, INPUT);
  pinMode(MQd, INPUT);
  pinMode(YL, INPUT);
  pinMode(ML, INPUT);
  
  sensors.begin();
 
  if (!bmp.begin()) {
    Serial.println("Não foi encontrado um sensor BMP085/BMP180 valido! Cheque o sensor!");
  while (1) {}
  }
}
  
void loop() {
  
  
  //Ler e enviar a Temperatura Externa
  float t1;
  t1 = sensors.getTempCByIndex(0);
  sensors.requestTemperatures();
  Serial.print("Temperatura Externa = ");
  Serial.print(sensors.getTempCByIndex(0));
  Serial.println(" *C");
  sendHttpRequest("MOV-01", (String)t1, sendData);

  //Ler e enviar o nivel UV
  int uvLevel = analogRead(ML);
  float outputVoltage = 3.3 * uvLevel/4095;
  float uvIntensity = map(outputVoltage, 0.99, 2.9, 0.0, 15.0);

  Serial.print(" UV Intensity: ");
  Serial.print(uvIntensity);
  Serial.print(" mW/cm^2");
  Serial.print(ML);
  Serial.print(" mW/cm^2");
  Serial.println();
  sendHttpRequest("MOV-02", (String)ML, sendData);
  
  //Ler e enviar a detecção de chuva 
  Serial.print("Chuva = ");
  Serial.println(analogRead(YL));
     if(analogRead(YL) >600) 
        sendHttpRequest("MOV-03", "SEM CHUVA", sendData);
     else
        sendHttpRequest("MOV-03", "COM CHUVA", sendData);
  
  //Ler e enviar a detecção de gás 
  Serial.print("Gas = ");
  Serial.println(analogRead(MQa));
  Serial.print("Gas - Digital = ");
  Serial.println(digitalRead(MQd));
    if (digitalRead(MQd) == 1) 
      sendHttpRequest("MOV-04", "GÁS DETECTADO", sendData);
    else
      sendHttpRequest("MOV-04", "GÁS NÃO DETECTADO", sendData);
   
  //Ler e enviar a Temperatura Interna 
  float t2;
  t2 = bmp.readTemperature();
  Serial.print("Temperatura Interna = ");
  Serial.print(bmp.readTemperature());
  Serial.println(" *C");
  sendHttpRequest("MOV-05", (String)t2, sendData);
  
  //Ler e enviar a Pressão
  float p;
  p = bmp.readPressure();  
  Serial.print("Pressão = ");
  Serial.print(bmp.readPressure());
  Serial.println(" Pa");
  sendHttpRequest("MOV-06", (String)p, sendData);
  
  // Ler e enviar a Altitude
  // pressure of 1013.25 millibar = 101325 Pascal
  float a;
  a = bmp.readAltitude();
  Serial.print("Altitude = ");
  Serial.print(bmp.readAltitude());
  Serial.println(" meters");
  sendHttpRequest("MOV-07", (String)a, sendData);
  
  //Ler e enviar a Altitude em relação ao nivel do mar
  float p2;
  p2 = bmp.readSealevelPressure(); 
  Serial.print("Pressure at sealevel (calculated) = ");
  Serial.print(bmp.readSealevelPressure());
  Serial.println(" Pa");
  sendHttpRequest("MOV-08", (String)p2, sendData);

  Serial.println();
  sendData = true;
  delay(2000);

}
