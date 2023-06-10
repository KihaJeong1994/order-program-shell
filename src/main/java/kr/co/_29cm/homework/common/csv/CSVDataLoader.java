package kr.co._29cm.homework.common.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import kr.co._29cm.homework.domain.product.entity.Product;
import kr.co._29cm.homework.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CSVDataLoader implements InitializingBean {

    private String CSV_PATH = "./data/data.csv";
    private final ProductService productService;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Product> products = loadProducts();
        productService.saveProducts(products);
    }

    private List<Product> loadProducts() throws IOException {
        try {
            CsvSchema bootstrapSchema = CsvSchema.builder()
                    .addColumn("상품번호")
                    .addColumn("상품명")
                    .addColumn("판매가격")
                    .addColumn("재고수량")
                    .build().withSkipFirstDataRow(true);
            CsvMapper mapper = new CsvMapper();
            ClassPathResource classPathResource = new ClassPathResource(CSV_PATH);
            InputStream inputStream = classPathResource.getInputStream();
            File file = File.createTempFile("data","csv");
            try {
                FileUtils.copyInputStreamToFile(inputStream,file);
            }catch (Exception ex){
                IOUtils.closeQuietly(inputStream);
            }
            MappingIterator<Product> readValues =
                    mapper.readerFor(Product.class).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
