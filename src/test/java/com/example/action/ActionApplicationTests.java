package com.example.action;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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

    @Test
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

                    String code = "";
                    switch (cell.getCellType()) {
                        case STRING:
                            data.add(cell.getStringCellValue());
                            break;
                        default:
                            data.add("");
                    }
                    columnCnt++;

                    if(columnCnt > 2) break;
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
//        Tag tag = new Tag().withKey("AppOwner").withValue(data.get(2));
//        CreateTagsRequest createTagsRequest = new CreateTagsRequest()
//                .withResources(data.get(1))
//                .withTags(tag);
//
//        ec2Client.createTags(createTagsRequest);
        System.out.println(data);
    }

    public static void setRdsTag(List<String> data, AmazonRDS rdsClient){
//        com.amazonaws.services.rds.model.Tag tag = new com.amazonaws.services.rds.model.Tag().withKey("AppOwner").withValue(data.get(2));
//        AddTagsToResourceRequest request = new AddTagsToResourceRequest()
//                .withResourceName(data.get(1))
//                .withTags(tag);
//
//        rdsClient.addTagsToResource(request);
        System.out.println(data);
    }

}
