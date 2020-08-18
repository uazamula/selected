import java.util.Arrays;
import java.util.Scanner;

/**
1) If lift move within a floor, the buttons on this floor don't work
2) If button is 'on', it's lighting up
3)howFullIs==mode
4)[]-door is close
5)][-door is open
6)° - call button from floor
7)· - floor button to floor
8) The lift doesn't change its direction in most cases
9)
 */
public class ResidentialBuilding2 {
    static int numFloor=12;
    static int limitOfWeight = 500;//kg
    static double coefOfFullness = 0.9;
    static int isFullByMoreThan =(int)(limitOfWeight*coefOfFullness);
    static int weight=0;
    static int amountOfPeople=0;
    static boolean[] floorFrom = new boolean[numFloor];
    static boolean[] floorTo = new boolean[numFloor];
    static int[] floorFromInt = new int[numFloor-1];
    static int position=0;//initialisation
    static int x=-1; //decision variable; it must be negative here
    static int howFullIs = 0;//0-Empty, 1 - Partially full, 2 - Full
    static int direction = 0;// -1 - down, 0 - stand, +1 - up
    static int rushHourMode = 0;// 0 - no RH, 1 - RHUp
    static boolean isMove=false;
    static boolean doorIsClosed;
    static boolean isFirstTime=true;
    static boolean isNotPressed=true;
    static int isOut=0;


    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        boolean isLoop=true;
        Arrays.fill(floorFrom,false);
        Arrays.fill(floorTo,false);
        doorIsClosed = true;
        isMove = false;

        System.out.print("Enter floor from or enter " + numFloor + " to quit:) ");
        floorFromInt[0]=kb.nextInt();//input
        if (floorFromInt[0]>=0&&floorFromInt[0]<numFloor) {
            floorFrom[floorFromInt[0]] = true;
            System.out.print("Enter an initial position (from 0 to " + (numFloor-1)+ "): ");
            position = kb.nextInt();//input
        }
        else
            isLoop = false;
        x=floorFromInt[0];

        while(isLoop) {

            display();
            if (isFirstTime)
                mainLogic();
            floorFrom = press(0);
            if (howFullIs != 0)
                floorTo = press(2);
            mainLogic();
            display();

            //Populate, depopulate and undo buttons on 'this' floor
            if (x==position){
                if(isMove)
                    isMove=false;
                doorIsClosed=false;
                rushHourMode = getInteger(0,1,"rush hour mode");
                System.out.println("Weight is " + weight + ". Limit is " + limitOfWeight
                        + ". It's full if > " + isFullByMoreThan);
                amountOfPeople = getInteger(-weight,limitOfWeight-weight,
                        "weight passengers going in(+)/ out(-),kg");
                weight = weight +amountOfPeople;
                System.out.println("Now weight is " + weight);
                if(weight<20) {
                    howFullIs = 0;
                    Arrays.fill(floorTo,false);
                }

                else if (weight<=isFullByMoreThan)
                    howFullIs=1;
                else
                    howFullIs=2;

                //setup direction if end point is reached
                doorIsClosed=true;
                resetDirection();

                floorFrom[position] = false;
                floorTo[position] = false;
                try {
                    System.out.print("Continue? yes - any int, no - any noninteger: ");
                    isOut=kb.nextInt();
                }
                catch(Exception e){
                    isLoop=false;
                }

            }
            else{
                if(!isMove)
                    isMove=true;
                if(x>position)
                    position++;
                else
                    position--;
            }
            isFirstTime=false;

        }
    }
     public static void resetDirection(){
            if (farthestUp(position, howFullIs) == position && direction == 1)
                direction = 0;
            if (farthestDown(position, howFullIs) == position && direction == -1)
                direction = 0;
    }

    public static int farthestUp(int x, int mode){
        int xx=x;//it's important
        boolean[] arr= new boolean[numFloor];
        switch (mode) {
            case (0): arr = floorFrom; break;
            case (2): arr = floorTo; break;
            case (1): for (int i=0; i<numFloor; i++)
                        arr[i] = floorFrom[i]||floorTo[i];
                    break;
        }
        if (x<numFloor-1)
            for(int i = x; i< numFloor; i++)
                if (arr[i])
                    xx = i;
        return xx;
    }
    public static int farthestDown(int x, int mode){
        int xx=x;//it's important
        boolean[] arr= new boolean[numFloor];
        switch (mode) {
            case (0): arr = floorFrom; break;
            case (2): arr = floorTo; break;
            case (1): for (int i=0; i<numFloor; i++)
                arr[i] = floorFrom[i]||floorTo[i];
                break;
        }
        if (x>0)
            for(int i = x; i>=0; i--)
            if (arr[i])
                xx = i;
        return xx;
    }

    public static int nearest(int x, int mode, int direction){
        int xx=x;//-1 means :There is no from/to in this direction
        boolean isLoop=true;
        int diff =999;
        int min = 999;
        boolean[] arr= new boolean[numFloor];
        switch (mode) {
            case (0): arr = floorFrom; break;
            case (2): arr = floorTo; break;
            case (1): for (int i=0; i<numFloor; i++)
                arr[i] = floorFrom[i]||floorTo[i];
                break;
        }
        switch (direction) {
            case(-1): // down
                for (int i = x; i >= 0 && isLoop; i--)
                    if (arr[i]){
                        xx = i;
                     isLoop = false;
                    }
                break;

            case(0)://all directions
                for (int i = 0; i < numFloor; i++)
                    if (arr[i]) {
                        diff = Math.abs(x - i);
                        if (diff < min) {
                         min = diff;
                            xx = i;
                        }
                    }
                break;

            case(1):// up
                for(int i = x; i< numFloor && isLoop; i++)
                    if (arr[i]){
                        xx = i;
                        isLoop = false;
                    }
                break;
        }
        return xx;
    }
    public static void display() {
        System.out.println("INFO");
        System.out.println("Lift's position: " + position);
        System.out.println("Rush hour mode: " + rushHourMode);
        //System.out.println("Call From: " + Arrays.toString(floorFrom));
        //System.out.println("Call To: " + Arrays.toString(floorTo));
        System.out.println("Is full? (0-empty, 1 - neither empty nor full, 2 - full): "
                + howFullIs);
        System.out.println("direction: " + direction);

        int to = 183;
        int from = 176;
        int underline = 95;
        int space = 32;
        int it1 = space, it2 = underline, it3 = space;

        System.out.println("   ___________");
        for (int i = numFloor - 1; i >= 0; i--) {
            if (floorFrom[i])
                it2 = from;
            if (floorTo[i])
                it1 = to;
            if (i == position) {
                it1 = 91;
                it3 = 93;
            }
            System.out.printf("%2d", (i + 0));
            if(x!=position)
                System.out.println(" ____" + (char) it1 + (char) it3 + (char) it2 + "____");
            else
                System.out.println(" ____" + (char) it3 + (char) it1 + (char) it2 + "____");
            it1 = space;
            it3 = space;
            it2 = underline;
        }
        if(x!=position || direction!=0)
            System.out.println("We are going to: " + x);
        else
            System.out.println("We are here (" + x + ")");
        System.out.println();
    }

    public static boolean[] press(int fromto) {
        int j,k,getInt,max=2;
        String s,s3="";
        boolean arr[];
        if(fromto==0) {
            s = "call button from a floor";
            arr=floorFrom;
        }
        else {
            s = "floor button to a desired floor";
            arr=floorTo;
        }
        System.out.print("Would you like press " + s + "? ");
        j = getInteger(1,max,"1 - yes, 2 - no" + s3);
        if (j==1) {
           k = getInteger(1,numFloor-1,"how many " +s);
           for (int i = 0; i < k; i++) {
               getInt = getInteger(0 + 0, numFloor - 1 + 0, s + (i + 1));
               if(position!=getInt||(howFullIs==0&&direction==0))
                   arr[getInt - 0] = true;
               //else a button is not allowed to press, if there are people in the lift on the same floor
               //or it is already moving empty within the same floor
           }
        }
        return arr;
    }
    public static int getInteger(int minID, int maxID, String s){
        Scanner kb = new Scanner(System.in);
        int id=-1;
        int quit = maxID+1;
        boolean isLoop=true;
        while(id!=quit && isLoop){
            try{
                System.out.print("Enter " + s + " (" + minID + " - " + maxID +")");
                if (s.toLowerCase().equals("id"))
                    System.out.print(" or " + quit + " to quit: ");
                else {
                    System.out.print(": ");
                    quit=Integer.MIN_VALUE;
                }
                id = kb.nextInt();
                if (id >= minID && id <= maxID)
                    isLoop = false;
                else if (id!=quit)
                    System.out.println("This value is out of range. Try again.");
            }
            catch(Exception e){
                System.out.println("Format you entered is not valid. Try again.");
                kb.nextLine();
            }
        }
        return id;
    }

    public static void mainLogic() {
         switch (direction) {
            case (-1):
                    x = nearest(position, howFullIs, -1);
                break;
            case (1):
                if (howFullIs == 0)
                    x = farthestUp(position, howFullIs);
                else//if 1,2
                    x = nearest(position, 2, 1);//
                break;
            case (0):
                if (howFullIs == 0) {
                    if (isFirstTime)
                        x = floorFromInt[0];//the first pressed call button
                    else
                    if (farthestUp(position, 0) == position)
                        x = nearest(position, 0, 0);
                    else
                        x = farthestUp(position, 0);
                }
                if (howFullIs==1||howFullIs==2){
                    if (farthestUp(position, howFullIs) == position)
                        x = nearest(position, howFullIs, 0);
                    else
                        x = nearest(position, 2,0);
                }
                if (x > position)
                    direction = 1;
                else
                    direction = -1;
                break;
        }
        isNotPressed=true;
        if (rushHourMode==1 &&howFullIs==0 && position!=0) {
            for(int i = 0; i<numFloor&&isNotPressed;i++)
                if (floorFrom[i]==true)
                    isNotPressed=false;
            if (isNotPressed==true) {
                direction = -1;
                x = 0;
                floorFrom[0]=true;
            }
        }
    }
}
