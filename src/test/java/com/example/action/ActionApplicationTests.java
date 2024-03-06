package com.example.action;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.model.AddTagsToResourceRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class ActionApplicationTests {

//    @Test
    void contextLoads() throws IOException {
        List<List<String>> list = getExcel();
        List<List<String>> ec2List = list.stream().filter( e -> e.get(0).equals("ec2") || e.get(0).equals("ebs")).collect(Collectors.toList());
        List<List<String>> rdsList = list.stream().filter( e -> e.get(0).equals("rds")).collect(Collectors.toList());

        AmazonEC2 ec2Client = AmazonEC2ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider("insw-dev"))
                .build();
        for(List<String> resource : ec2List) {
            setEc2Tag(resource, ec2Client);
        }

        AmazonRDS rdsClient = AmazonRDSClientBuilder.standard()
                .withRegion("ap-northeast-2")
                .withCredentials(new ProfileCredentialsProvider("insw-dev"))
                .build();

        for(List<String> resource : rdsList) {
            setRdsTag(resource, rdsClient);
        }

    }

    public static List<List<String>> getExcel() throws IOException {
        List<List<String>> list = new ArrayList();

        try {
            FileInputStream file = new FileInputStream("C:\\Users\\youngho\\Downloads\\tags.xlsx");
            IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                List<String> data = new ArrayList();
                int columnCnt = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case STRING:
                            data.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            data.add(NumberToTextConverter.toText((cell.getNumericCellValue())));
                            break;
                        default:
                            data.add("");
                    }
                    columnCnt++;

                    if(columnCnt > 3) break;
                }

                list.add(data);
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void setEc2Tag(List<String> data, AmazonEC2 ec2Client){
        try {
            if (data.size() > 2) {
                Tag appOwnerCd = new Tag().withKey("AppOwnerCd").withValue(data.get(3));
                Tag appOwnerNm = new Tag().withKey("AppOwnerNm").withValue(data.get(2));
                CreateTagsRequest createTagsRequest = new CreateTagsRequest()
                        .withResources(data.get(1))
                        .withTags(appOwnerCd, appOwnerNm);

                ec2Client.createTags(createTagsRequest);
            }
        }catch(Exception e){
        }
    }

    public static void setRdsTag(List<String> data, AmazonRDS rdsClient){
        try {
            if (data.size() > 2) {
                com.amazonaws.services.rds.model.Tag appOwnerCd = new com.amazonaws.services.rds.model.Tag().withKey("AppOwnerCd").withValue(data.get(3));
                com.amazonaws.services.rds.model.Tag appOwnerNm = new com.amazonaws.services.rds.model.Tag().withKey("AppOwnerNm").withValue(data.get(2));
                AddTagsToResourceRequest request = new AddTagsToResourceRequest()
                        .withResourceName(data.get(1))
                        .withTags(appOwnerCd, appOwnerNm);

                rdsClient.addTagsToResource(request);
            }
        }catch(Exception e){
        }
    }

//    @Test
    void getTeamCode() throws IOException {
        List<List<String>> list = new ArrayList();

        try {
            FileInputStream file = new FileInputStream("C:\\Users\\youngho\\Downloads\\team.xlsx");
            IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                List<String> data = new ArrayList();
                int columnCnt = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case STRING:
                            if(columnCnt == 0 || columnCnt == 1) data.add(cell.getStringCellValue());
                            break;
                        default:
                            data.add("");
                    }
                    columnCnt++;
                }

                list.add(data);
            }
            file.close();

            for(List<String> d: list){
                System.out.println(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
