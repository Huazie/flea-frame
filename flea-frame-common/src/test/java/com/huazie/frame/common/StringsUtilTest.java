package com.huazie.frame.common;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huazie.frame.common.util.StringUtils;

/**
 *  
 * @author huazie
 * @version v1.0.0
 * @date 2018年1月25日
 *
 */
public class StringsUtilTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringsUtilTest.class);
	
	@Test
	public void testStringBefore(){
		StringsUtilTest.LOGGER.debug(StringUtils.subStrBefore("asda", 0));
		StringsUtilTest.LOGGER.debug(StringUtils.subStrBefore("asda", -1));
		StringsUtilTest.LOGGER.debug(StringUtils.subStrBefore("asda", 2));
		StringsUtilTest.LOGGER.debug(StringUtils.subStrBefore("asda", 5));
	}
	
	@Test
	public void testStringLast(){
		StringsUtilTest.LOGGER.debug(StringUtils.subStrLast("asda", 0));
		StringsUtilTest.LOGGER.debug(StringUtils.subStrLast("asda", -1));
		StringsUtilTest.LOGGER.debug(StringUtils.subStrLast("asda", 2));
		StringsUtilTest.LOGGER.debug(StringUtils.subStrLast("asda", 5));
	}
	
	@Test
	public void testStringCat(){
		StringsUtilTest.LOGGER.debug(StringUtils.strCat("flea_file_info", "_" , "ff"));
	}
	
	@Test
	public void testReplace(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("INSERT INTO {##table##} ( {##columns##} ) VALUES( {##values##} )");
		StringsUtilTest.LOGGER.debug(strBuilder.toString());
		String placeholder = "{##table##}";
		String str = "flea_file_info_ff";
		try {
			StringUtils.replace(strBuilder, placeholder, str);
		} catch (Exception e) {
			StringsUtilTest.LOGGER.error("Exception={}", e);
		}
		StringsUtilTest.LOGGER.debug(strBuilder.toString());
	}
	
	@Test
	public void testSplit(){
		String[] strs = StringUtils.split(" para_id = :paraId, para_type = :paraType", ",");
		if(!ArrayUtils.isEmpty(strs)){
			for(String str : strs){
				StringsUtilTest.LOGGER.debug("Str={}", str);
			}
		}else{
			StringsUtilTest.LOGGER.debug("null");
		}
	}
}
