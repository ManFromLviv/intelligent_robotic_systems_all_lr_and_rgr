import muvium.compatibility.arduino.*; 

public class Class0 extends Arduino{ 
	
	// Змінні, які зберігають номер вихідних пінів діодів
	int ledStatus = 13;
	int ledRoomOne = 12;
	int ledRoomTwo = 11;
	int ledRoomThree = 10;
	
	// Аналогові входи для сенсорів температури
	int sensorRoom1 = A4;
	int sensorValueRoom1 = 0;

	int sensorRoom2 = A5;
	int sensorValueRoom2 = 0;
	
	int sensorRoom3 = A6;
	int sensorValueRoom3 = 0;

	// Таймер-лічильник, використовується для контролю періодичності зчитування температури в різних кімнатах (Залежить від умови активації логіки A, B, C, D і затримки 6 секунд)
	int i = 0;

	// Змінні, які  читають вхідні логічні значення з перемикачів A, B, C, D 
	int logic1 = A0;
	int logic2 = A1;
	int logic3 = A2;
	int logic4 = A3;
	
	public void setup() {
		// Підключаємо серійний монітор для виводу в консолі
		Serial.begin(9600); 
		// Ініціалізуємо вихідні піни
		pinMode(ledStatus, OUTPUT);
		pinMode(ledRoomOne, OUTPUT);
		pinMode(ledRoomTwo, OUTPUT);
		pinMode(ledRoomThree, OUTPUT);
	}

	public void loop() {
		
		// Зчитуємо входи з перемикачів А, B, C, D та конвертуємо в логічний тип
		boolean A = digitalRead(logic1) == HIGH;
		boolean B = digitalRead(logic2) == HIGH;
		boolean C = digitalRead(logic3) == HIGH;
		boolean D = digitalRead(logic4) == HIGH;
		
		// Реалізуємо перевірку по функції
		if((A && B && !(C && D)  ) ||
			( !( A && B && C ) && D ) ||
			( !(A && !B) && C && !D )
		){
			digitalWrite(ledStatus, HIGH); // Вмикаємо світодіод у разі успіху
		}else{
			digitalWrite(ledStatus, LOW);// Вмикаємо світодіод, коли функція не справджується
		}
		// Реалізуємо перевірку по функції
		if(
			(A && B && !(C && D)  ) ||
			( !( A && B && C ) && D ) ||
			( !(A && !B) && C && !D )
		) {
			
			int h = (int) ((System.currentTimeMillis() / (1000 * 60 * 60)) % 24) + 3; // Отримуємо години з ноутбука
			int m = (int) ((System.currentTimeMillis() / (1000 * 60)) % 60); // Отримуємо хвилини з ноутбука
			
			// Перевікра діапазону для першої кімнати і включання відповідного світодіода
			if((h == 17 && m >= 10) || (h == 18 && m <= 30)){
				digitalWrite(ledRoomOne, HIGH);
			} else{
				digitalWrite(ledRoomOne, LOW);
			}
			// Перевікра діапазону для другої кімнати і включання відповідного світодіода
			if (h == 18 && m >= 20 && m <= 40){
				digitalWrite(ledRoomTwo, HIGH);
			} else{
				digitalWrite(ledRoomTwo, LOW);
			}
			// Перевікра діапазону для третьої кімнати і включання відповідного світодіода
			if((h == 21 && m >= 0 && m <= 10)){
				digitalWrite(ledRoomThree, HIGH);
			} else{
				digitalWrite(ledRoomThree, LOW);
			}
		}
		// Реалізуємо перевірку по функції
		if( 
			(A && B && !(C && D)  ) ||
			( !( A && B && C ) && D ) ||
			( !(A && !B) && C && !D )
		) {
			// Збільшуємо лічильник діапазонів
			i++;
			// Встановлюємо затримку
			delay(6000);
			
			int h = (int) ((System.currentTimeMillis() / (1000 * 60 * 60)) % 24) + 3; // Отримуємо години з ноутбука
			int m = (int) ((System.currentTimeMillis() / (1000 * 60)) % 60); // Отримуємо хвилини з ноутбука
			
			if(i % 180 == 0){
				sensorValueRoom1 = analogRead(sensorRoom1);

				double millivolts = (sensorValueRoom1 / 1024.0) * 5000; 
				double Celsius = millivolts / 10;
				
				Serial.println("Room 1 Temperature = " + Celsius + " | " + h + ":" + m);	
			}
			if(i % 120 == 0){
				sensorValueRoom2 = analogRead(sensorRoom2);

				double millivolts = (sensorValueRoom2 / 1024.0) * 5000; 
				double Celsius = millivolts / 10;
				
				Serial.println("Room 2 Temperature = " + Celsius + " | " + h + ":" + m);
			}
			if(i % 10 == 0){
				sensorValueRoom3 = analogRead(sensorRoom3);

				double millivolts = (sensorValueRoom3 / 1024.0) * 5000; 
				double Celsius = millivolts / 10;
				
				Serial.println("Room 3 Temperature = " + Celsius + " | " + h + ":" + m);	
			}
		}
		
	}
	
}
