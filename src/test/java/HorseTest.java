import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class HorseTest {

    @Test
    public void nullNameException(){
        assertThrows(IllegalArgumentException.class, () -> new Horse(null, 1,1));
    }

    @Test
    public void nullNameMessage(){
       try {
           new Horse(null, 1, 1);
           fail();
       }catch (IllegalArgumentException e){
           assertEquals("Name cannot be null.", e.getMessage());
       }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "\t\t", "\n\n\n\n\n"})
    public void blankNameException(String name){
        IllegalArgumentException e =  assertThrows(IllegalArgumentException.class, () -> new Horse(name, 1,1));
        assertEquals("Name cannot be blank.", e.getMessage());
    }

    @Test
    public void negativeNumberSpeedException(){
        IllegalArgumentException e =  assertThrows(IllegalArgumentException.class, () -> new Horse("Бегунья", -1,1));
        assertEquals("Speed cannot be negative.", e.getMessage());
    }

    @Test
    public void negativeNumberDistanceException(){
        IllegalArgumentException e =  assertThrows(IllegalArgumentException.class, () -> new Horse("Бегунья", 1,-1));
        assertEquals("Distance cannot be negative.", e.getMessage());
    }

    @Test
    public void getName() throws NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("Бегун", 2, 4);

        Field name = Horse.class.getDeclaredField("name");
        name.setAccessible(true);
        String nameValue = (String) name.get(horse);
        assertEquals("Бегун", nameValue);

    }

    @Test
    public void getSpeed(){
        Horse horse = new Horse("Бегун", 2, 4);
        assertEquals(2,  horse.getSpeed());

    }

    @Test
    public void getDistance(){
        Horse horse = new Horse("Бегун", 2, 4);
        assertEquals(4,  horse.getDistance());
    }

    @Test
    public void zeroDistanceByDefault(){
        Horse horse = new Horse("Бегун", 2);
        assertEquals(0,  horse.getDistance());
    }

    @Test
    public void moveUsesGetRandom(){
        try(MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)){
            new Horse("Конь", 2, 5).move();

            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.2, 0.3, 0.5, 0.8})
    public void moveUsesFormulaDistancePlusSpeedMultiplyGetRandom(double number){
        try(MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)){
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(number);
            Horse horse = new Horse("Бегун", 2, 6);
            double expected = horse.getDistance() + horse.getSpeed() * number;
            horse.move();
            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
            assertEquals(expected, horse.getDistance());
        }
    }


}
