import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.HashMap;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0,n).forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}
/*
Intervals [1-3] repeat I
[4] midInterval special case IV [5] midInterval V
[6-8] repeat VI, VII, VIII
[9] special case IX [10] midInterval X
[11 - 39] repeat XI - XXXIX number/10 repeat and number%10 repeats - cycle
[40] special case XL
[50] midInterval L
[90] special case XC
[100] midInterval C
[400] special case CD
[500] midInterval D
[900] special case CM
[1000] midInterval M
 */
class RomanConverter {

    private static String getRoman(int n)
    {
        HashMap<Integer,String> map = new HashMap<>();
        map.put(1,"I"); map.put(4,"IV"); map.put(5,"V");
        map.put(9,"IX"); map.put(10,"X"); map.put(40,"XL");
        map.put(50,"L"); map.put(90,"XC"); map.put(100,"C");
        map.put(400,"CD"); map.put(500,"D"); map.put(900,"CM");
        map.put(1000,"M");
        return map.get(n);
    }

    public static String toRoman(int n){
        StringBuilder string = new StringBuilder();
        int remainder = 0;
        int roman = 0;
        while(n>0)
        {
            if(n>=1000)
            {
                remainder = n%1000;
                roman = n/1000;
                for(int i=0; i<roman;i++)
                    string.append(getRoman(1000));
            }
            else if(n>=500 && n<=999)
            {
                remainder = n%100;
                roman = n/100;
                if(roman == 9)
                    string.append(getRoman(900)); //special cases
                else if(roman == 5)
                    string.append(getRoman(500)); //special cases
                else
                {
                    string.append(getRoman(500)); //unnecessary code fix!
                    for(int i=0;i<roman-5;i++)
                        string.append(getRoman(100));
                }
            }
            else if(n>=100 && n<=499)
            {
                remainder = n%100;
                roman = n/100;
                if(roman == 4)
                    string.append(getRoman(400));
                else
                {
                    for(int i=0;i<roman;i++)
                        string.append(getRoman(100));
                }
            }
            else if(n>=50 && n<=99)
            {
                remainder = n%10;
                roman = n/10;
                if(roman == 9)
                    string.append(getRoman(90)); // special case
                else if(roman == 5)
                    string.append(getRoman(50)); // special case
                else
                {
                    string.append(getRoman(50)); // same here
                    for(int i=0;i<roman-5;i++)
                        string.append(getRoman(10));
                }
            }
            else if(n>=10 && n<=49)
            {
                remainder = n%10;
                roman = n/10;
                if(roman == 4)
                    string.append(getRoman(40)); // special case
                else{
                    for(int i=0;i<roman;i++)
                        string.append(getRoman(10));
                }
            }
            else if(n>=5 && n<=9)
            {
                remainder = 0; roman = n;
                if(roman == 5)
                    string.append(getRoman(5));
                else if(roman == 9)
                    string.append(getRoman(9));
                else{
                    string.append(getRoman(5));
                    for(int i=0;i<roman-5;i++)
                        string.append(getRoman(1));
                }
            }
            else
            {
                remainder = 0; roman = n;
                if(roman == 4)
                    string.append(getRoman(4));
                else
                {
                    for(int i=0;i<roman;i++)
                        string.append(getRoman(1));
                }
            }

            n = remainder;
        }
        return string.toString();
    }
}