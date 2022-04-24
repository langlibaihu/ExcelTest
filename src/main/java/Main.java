import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            ReadExcel("2022.xls", "饼图.xls");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void ReadExcel(String input, String output) throws Exception {

        FileOutputStream os = new FileOutputStream(output);  //文件流

        //创建工作薄
        WritableWorkbook writableWorkbook = Workbook.createWorkbook(os);
        //创建新的一页
        WritableSheet writableSheet = writableWorkbook.createSheet("饼图", 0);

        //1:创建workbook
        Workbook workbook = Workbook.getWorkbook(new File(input));   //这里输入表格文档的路径及名字
        //2:获取第一个工作表sheet
        Sheet sheet = workbook.getSheet(0);  //下标从0开始表示第一个工作表
        //3:获取数据

        int col = 0;
        for (int i = 5; i < sheet.getColumns(); i++) {
            String title = sheet.getCell(i, 0).getContents();
            Map<String, Integer> map = new HashMap<>();
            for (int j = 1; j < sheet.getRows(); j++) {
                String[] strings = sheet.getCell(i, j).getContents().split(",");
                for (String str :
                        strings) {
                    if (str.charAt(0) == ' ') {
                        str = str.substring(1);
                    }
                    if (map.containsKey(str)) {
                        map.put(str, map.get(str) + 1);
                    } else {
                        map.put(str, 1);
                    }
                }
            }
            ChartUtils.createPieChart(map,title+".jpg",title);
            //写入
            jxl.write.Label l = new jxl.write.Label(col, 0, title);
            writableSheet.addCell(l);
            int row = 1;
            for (Map.Entry<String, Integer> entry :
                    map.entrySet()) {
                jxl.write.Label l1 = new jxl.write.Label(col, row, entry.getKey());
                jxl.write.Number l2 = new jxl.write.Number(col + 1, row++, entry.getValue());

                writableSheet.addCell(l1);
                writableSheet.addCell(l2);
            }
            col += 3;
        }
        //4：关闭资源
        writableWorkbook.write();
        writableWorkbook.close();
        os.close();
        workbook.close();
    }

}
