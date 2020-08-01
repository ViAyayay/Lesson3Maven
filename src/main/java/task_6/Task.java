package task_6;

public class Task {

    public int[] doTask1(int[] arr){
        int[] newArr;
        for (int i = arr.length-1; i >= 0; i--) {
            if (arr[i]==4){
                newArr = new int[arr.length-i-1];
                System.arraycopy(arr, i+1, newArr,0, newArr.length);
                return newArr;
                }
            }
        throw new RuntimeException();
    }

    public boolean doTask2(int[] arr){
        boolean one = false;
        boolean four = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]==1||arr[i]==4){
                if(arr[i]==1) one = true;
                if(arr[i]==4) four = true;
            }else{return false;}
        }
        return (one&&four) ? true : false;
    }
}
