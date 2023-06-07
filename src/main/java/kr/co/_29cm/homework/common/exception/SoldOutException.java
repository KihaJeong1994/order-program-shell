package kr.co._29cm.homework.common.exception;

public class SoldOutException extends CustomException {
    @Override
    public String getMessage() {
        return "SoldOutException 발생. 주문한 상품량이 재고량보다 큽니다.";
    }
}
