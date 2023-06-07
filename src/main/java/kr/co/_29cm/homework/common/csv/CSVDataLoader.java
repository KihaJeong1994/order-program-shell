package kr.co._29cm.homework.common.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import kr.co._29cm.homework.product.entity.Product;
import kr.co._29cm.homework.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CSVDataLoader implements InitializingBean {

    private String CSV_PATH = "data/data.csv";
    private final ProductRepository productRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Product> products = loadObjectList();
        productRepository.saveAll(products);
    }

    public List<Product> loadObjectList() throws IOException {
        try {
            CsvSchema bootstrapSchema = CsvSchema.builder()
                    .addColumn("상품번호")
                    .addColumn("상품명")
                    .addColumn("판매가격")
                    .addColumn("재고수량")
                    .build().withSkipFirstDataRow(true);
            CsvMapper mapper = new CsvMapper();
            File file = new ClassPathResource(CSV_PATH).getFile();
            MappingIterator<Product> readValues =
                    mapper.reader(Product.class).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

}
