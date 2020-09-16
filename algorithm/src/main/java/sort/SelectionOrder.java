package sort;

/**
 * 选择法排序
 * @author tao.yang
 * @date 2020-09-16
 */
public class SelectionOrder {

    public static void main(String[] args) {
        int[] sorts = {2, 3, 5, 1, 9, 4, 7, 6};

        for (int i = 0; i < sorts.length; i++){

            int selectionIndex = i;

            for (int j = i + 1; j < sorts.length; j++){
                if (sorts[selectionIndex] < sorts[j]){
                    selectionIndex = j;
                }
            }

            if (selectionIndex != i){
                int tmp = sorts[selectionIndex];
                sorts[selectionIndex] = sorts[i];
                sorts[i] = tmp;
            }
        }

        for (int i = 0; i < sorts.length; i++) {
            System.out.print(sorts[i] + " ");
        }
    }

}
