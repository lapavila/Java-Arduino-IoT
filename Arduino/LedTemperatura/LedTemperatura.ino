int led = 13;
int entrada = 0;
int light = LOW;

int pinoSensor = 2;
int valorLido = 0;
float temperatura = 0;

boolean initiated = false;
void setup() {   
  Serial.begin(9600);  
  pinMode(led, OUTPUT);     
}

void loop() {
  if (!initiated) {
    digitalWrite(led, light);
    initiated = true;
  }
  if (Serial.available() > 0) {
    entrada = Serial.read();
    light = light == HIGH ? LOW : HIGH;
    digitalWrite(led, light);
  }
  
  valorLido = analogRead(pinoSensor);
  temperatura = (valorLido * 0.00488 * 100);
  Serial.println(temperatura);
  delay(500);
}


