#include <DHT.h>
#include <SPI.h>
#include <UIPEthernet.h>

#define TYPEDHT DHT22
#define PINDHT A0  
#define PINSOILHUMIDITY A1
#define PINWATERFLOW 2
#define PINVIBRATION 3
#define PINFIRESENSOR 4
#define PINPIR1 5
#define PINPIR2 6
#define PIRLED 7
#define TIMEDELAY 10

DHT dht(PINDHT, TYPEDHT); 
EthernetClient client;
IPAddress hostIP(192,168,198,105);
uint16_t hostPort = 8090;
int waterFlowPulse;
float temperature = 0;
float humidity = 0;
int timeDelay = 0;
bool sendData = false;

void setup() {
  uint8_t mac[6] = {0x00,0x01,0x02,0x03,0x04,0x05};
  
  pinMode(PINSOILHUMIDITY, INPUT);
  pinMode(PINFIRESENSOR, INPUT);
  pinMode(PINVIBRATION, INPUT);
  pinMode(PINWATERFLOW, INPUT);
  pinMode(PINPIR1, INPUT);
  pinMode(PINPIR2, INPUT);
  pinMode(PIRLED, OUTPUT);

  attachInterrupt(0, waterFlowPulseinc, RISING);
  
  Serial.begin(9600);
  Ethernet.begin(mac);
  dht.begin(); 
}

void loop() {
  int pinValue;
  float h;
  float t;
  
  h = dht.readHumidity();
  t = dht.readTemperature();
  
  if(!isnan(h))
    humidity = h;
  
  if(!isnan(t))
    temperature = t;

  sendHttpRequest("FIX-01", (String)temperature, sendData);
  sendHttpRequest("FIX-02", (String)humidity, sendData);

  waterFlowPulse = 0;
  
  sei();
  delay(1000);
  cli(); 

  sendHttpRequest("FIX-03", (String)(waterFlowPulse / 5.5), sendData);
  
  if(digitalRead(PINFIRESENSOR) != 1) 
    sendHttpRequest("FIX-04", "FOGO DETECTADO", sendData);
  else
    sendHttpRequest("FIX-04", "FOGO NAO DETECTADO", sendData);

  pinValue = checkVibration();

  if(pinValue <= 1000)
    sendHttpRequest("FIX-05", "VIBRACAO BAIXA", sendData);
  else if(pinValue <= 4000)
    sendHttpRequest("FIX-05", "VIBRACAO MEDIA", sendData);
  else
    sendHttpRequest("FIX-05", "VIBRACAO ALTA", sendData);

  pinValue = analogRead(PINSOILHUMIDITY);

  if(pinValue >= 0 & pinValue < 550)
     sendHttpRequest("FIX-06", "UMIDO", sendData); 
  else if(pinValue > 550 && pinValue < 800)
    sendHttpRequest("FIX-06", "MEIO UMIDO", sendData); 
  else
    sendHttpRequest("FIX-06", "SECO", sendData); 

   sendHttpRequest("XXXXXX", "X", sendData);
   
  if(digitalRead(PINPIR1) != 1 && digitalRead(PINPIR2) != 1) {
    digitalWrite(PIRLED, LOW);
  } else {
    digitalWrite(PIRLED, HIGH);
  }

  sei();
  
  if(timeDelay == 0) {
    sendData = true;
    timeDelay = TIMEDELAY;
  } else {
    sendData = false;
    timeDelay--;
    delay(1000);
  }
  
  cli(); 
}

long checkVibration(){
  return pulseIn(PINVIBRATION, HIGH);
}

void waterFlowPulseinc() {
  waterFlowPulse++;
}

void sendHttpRequest(String cod, String value, bool sendData) {
  sei();

  if(sendData) {
    String json = "{\"leitura\":{\"codigo\":\"" + cod + "\",\"valor\":\"" + value + "\"}}";
   
    Serial.println(json);
    
    /*while(!client.connect(hostIP, hostPort)) {   
      delay(100);
    }
  
    Serial.println("Enviando...");
    client.println("POST /teste_arduino_estacao/RealTime/ HTTP/1.1");
    client.println((String)"Host: " + hostIP[0] + "." + hostIP[1] + "." + hostIP[2] + "." + hostIP[3] + ":" + hostPort);
    client.println("User-Agent: Arduino/nano");
    client.println("Connection: close");
    client.print("Content-Length: ");
    client.println(json.length());
    client.println();
    client.println(json);
    client.stop();
    delay(1000);*/
  }
  
  cli();
}

