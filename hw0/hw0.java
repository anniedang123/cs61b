public class Maximum {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4};
        max(a);
    }

    public static int max(int[] a) {
        int ind = 0;
        int maxVal = 0;
        for(int x=1; x< a.length; x++) {
            if (a[ind]>maxVal) {
                maxVal = a[ind];
            }
            ind += 1;
        }
        return maxVal;
    }

    public static boolean threeSum(int[] c){
        for (int i =0; i<c.length; i++){
            for(int j =0; j<c.length; j++){
                for (int l =0; l<c.length; l++){
                    if(b[i]+b[j]+b[l] ==0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean threeSumDistinct(int[] b){
        for (int i =0; i<b.length; i++){
            for(int j =i+1; j<b.length; j++){
                for (int l =j+1; l<b.length; l++){
                    if(b[i]+b[j]+b[l] ==0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}