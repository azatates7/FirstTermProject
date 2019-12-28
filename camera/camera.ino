 #include </home/azatates7/Downloads/Project/camera/module.h>

void setup(){
  int pirPin = 9;  // PIR pin
  int deger = 0; // PIR value
  pinMode(8,OUTPUT);
  pinMode(pirPin, INPUT);     // PIR Pin'i giriş yapılıyor.
  Serial.begin(9600);           //Serial Porttan veri göndermek için baundrate ayarlanıyor.
  arduinoUnoInut();
  camInit();
  setResolution();
  setColor();
 writeReg(0x11, 10); 
}

void loop(){ 
  deger = digitalRead(pirPin);  // Dijital pin okunuyor
  Serial.println(deger);          // Okunan değer seri porttan okunuyor.
  if (deger == HIGH) {            
    captureImg(320, 240);
    digitalWrite(8,HIGH); 
    delay(2000);
    digitalWrite(8, LOW);
  }
  else{
    delay(2000);
    digitalWrite(8,LOW);
  } }
