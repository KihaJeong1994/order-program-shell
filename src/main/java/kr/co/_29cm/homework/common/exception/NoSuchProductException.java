package kr.co._29cm.homework.common.exception;

public class NoSuchProductException extends CustomException {
    private Long id;
    public NoSuchProductException(Long id) {
        this.id=id;
    }

    @Override
    public String getMessage() {
        return "NoSuchProductException 발생. "+id+" 상품번호를 가진 상품이 없습니다";
    }
}
