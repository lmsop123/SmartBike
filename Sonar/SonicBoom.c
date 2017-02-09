#include <stdio.h>
#include <stdlib.h>
#include <wiringPi.h>

#define TRIG 1
#define ECHO 4


void setup() {

     wiringPiSetup();
     pinMode(TRIG,OUTPUT);
     pinMode(ECHO,INPUT);


     digitalWrite(TRIG, LOW);
     delay(20);
}


int getDistance(){
   
    digitalWrite(TRIG,HIGH);
    delayMicroseconds(20);
    digitalWrite(TRIG,LOW);

    while(digitalRead(ECHO) == LOW);    
    
   long startTime = micros();

    while(digitalRead(ECHO) == HIGH);

    long travelTime = micros() - startTime; 

    int distance = travelTime / 58;

    return distance;
}


int main (void){

 setup();
 
  if(getDistance() < 50)
     printf("%d",1);
  else
     printf("%d",0);

 return 0;


}
