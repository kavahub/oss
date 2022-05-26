package cn.springseed.oss.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import cn.springseed.oss.common.util.OSSRuntimeException.BadRequest;
import cn.springseed.oss.common.util.OSSRuntimeException.NotFound;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class OSSRuntimeExceptionTests {

    @Test
    public void giveErrorCode_whenIsAssignableFrom_thenTrue() {
        final ErrorCode code = NotFound.OBJECT_ID_NOT_EXIST;
        assertThat(code instanceof ErrorCode).isTrue();
        assertThat(code instanceof NotFound).isTrue();
        assertThat(code instanceof BadRequest).isFalse();
    }
    
}
