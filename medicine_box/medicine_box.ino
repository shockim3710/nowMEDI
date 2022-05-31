#include <Stepper.h>
#include <SoftwareSerial.h>

int before = 3;
int TxPin = 2;
int RxPin = 3;
int Button = 4;
int ledPin1 = 13;
int LED= 12;

const int STEPS = 2048;
Stepper stepper(STEPS, 8,10,9,11);
SoftwareSerial BTSerial(TxPin, RxPin); 

void setup()  
{
  BTSerial.begin(9600);
  Serial.begin(9600);
  pinMode(LED,OUTPUT);
  pinMode(Button,INPUT);
  stepper.setSpeed(14);
}

void loop()
{
  if (BTSerial.available())
  {
      char cmd = (char)BTSerial.read();
      if(cmd == '0') {
        digitalWrite(LED,LOW);
      } 
      else if(cmd == '1') {
        digitalWrite(LED,HIGH);
      } 
      else if(cmd == '3') {
        
         if(before ==4){
            int val=-120;
            val=map(val,0,360,0,2048); //회전각 스템 수
            stepper.step(val);

         }
         else if(before == 5){
            int val=-240;
            val=map(val,0,360,0,2048); //회전각 스템 수
            stepper.step(val);
         }
         
        delay(10);
        before=3;
         
      }
      else if(cmd == '4') {
        
         if(before == 3) {
            int val=+120;
            val=map(val,0,360,0,2048); //회전각 스템 수
            stepper.step(val);
         }
         else if(before == 5){
            int val=-120;
            val=map(val,0,360,0,2048); //회전각 스템 수
            stepper.step(val);
         }
         
        delay(10);
        before=4;
         
      }
      else if(cmd == '5') {
        
         if(before ==3){
            int val=+240;
            val=map(val,0,360,0,2048); //회전각 스템 수
            stepper.step(val);
         }
         else if(before == 4){
            int val=+120;
            val=map(val,0,360,0,2048); //회전각 스템 수
            stepper.step(val);
         }
        delay(10);
        before=5;
      }
    }

    if (digitalRead(Button) == HIGH){
      BTSerial.println("2");
    }
}
