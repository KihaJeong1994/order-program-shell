package kr.co._29cm.homework.product.service;

import kr.co._29cm.homework.common.exception.SoldOutException;
import kr.co._29cm.homework.product.entity.Product;
import kr.co._29cm.homework.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final String WHITE_SPACE = "     ";
    private final String LONGER_WHITE_SPACE = "                          ";

    private final ProductRepository productRepository;

    public List<Product> getProducts(){
        return productRepository.findAllByOrderByIdDesc();
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }

    @Transactional
    public void saveProducts(List<Product> products){
        productRepository.saveAll(products);
    }

    @Transactional
    public void decreaseStock(Long id, int quantity) throws SoldOutException {
        Product product = productRepository.findByIdForUpdate(id);
        if(quantity> product.getStock()){
            throw new SoldOutException();
        }else {
            product.setStock(product.getStock()-quantity);
            productRepository.save(product);
        }
    }

    public void printProductList() {
        System.out.println("상품번호"+"   "+"상품명"+LONGER_WHITE_SPACE+"판매가격"+WHITE_SPACE+"재고수");
        getProducts().forEach(
            product -> {
                System.out.println(
                        product.getId()+WHITE_SPACE+product.getName()+WHITE_SPACE+product.getPrice()+WHITE_SPACE+product.getStock()
                );
            }
        );
        System.out.println("");
    }
}
