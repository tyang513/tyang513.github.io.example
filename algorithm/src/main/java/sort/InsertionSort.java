package sort;

/**
 * 插入法排序
 *
 * @author tao.yang
 * @date 2020-09-16
 */
public class InsertionSort {

    public static void main(String[] args) {
        int[] sorts = {2, 3, 5, 1, 9, 4, 7, 6, 8};

        for (int i = 1; i < sorts.length; i++) {
            int insertionValue = sorts[i];
            // 移动操作
            int j = i - 1;
            while (j >= 0 && insertionValue < sorts[j]){
                sorts[j + 1] = sorts[j];
                j--;
            }
            sorts[j + 1] = insertionValue;
        }

        System.out.println("=================");
        for (int m = 0; m < sorts.length; m++) {
            System.out.print(sorts[m] + " ");
        }

    }

}
