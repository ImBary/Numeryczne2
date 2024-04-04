import java.util.Random;
public class Monte {
    

    public Monte(){}


    public double monte(int N, int mStartu){
        double prob=0.0;
        for(int i=0; i<N; i++){
            prob+=wyjscieTest(mStartu);
        }
        return prob/N;
    }


    private static double wyjscieTest(int start){
        int wyjscie = start + 3;
        int osk = start - 2;
        int wyn=0;
        Random rand = new Random();
        while (start!=osk && start!=wyjscie) {
            if(rand.nextInt(2)==0){
                start-=1;
            }else{
                start+=1;
            }
            if(start==osk){
                wyn=0;
            }else if(start==wyjscie){
                wyn=1;
            }
        }

        return wyn;
    }
}

