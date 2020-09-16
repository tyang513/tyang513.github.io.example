package sort;

/**
 * @author tao.yang
 * @date 2020-09-15
 */
public class BubbleSort {

    public static void main(String[] args) {
        BubbleSort sort = new BubbleSort();
        int[] sorts = {2, 3, 5, 1, 9, 4, 7, 6, 8};
        int[] bubble = sort.bubble(sorts);

        for (int i = 0; i < bubble.length; i++) {
            System.out.print(sorts[i] + " ");
        }
    }


    public int[] bubble(int[] sorts){
        for (int i = 0; i < sorts.length; i++) {
            int tmp = 0;
            for (int j = 0; j < sorts.length; j++) {
                if (sorts[i] > sorts[j]) {
                    tmp = sorts[i];
                    sorts[i] = sorts[j];
                    sorts[j] = tmp;
                }
            }
        }
        return sorts;
    }


}
