/**
 * Copyright (c) 2014 by the original author or authors.
 *
 * This code is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package ch.sdi.core.impl.data.converter;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * TODO
 *
 * @version 1.0 (16.11.2014)
 * @author  Heri
 */
public class ConverterJpgFromHexDumpTest
{

    /** logger for this class */
    private Logger myLog = LogManager.getLogger( ConverterJpgFromHexDumpTest.class );

    /** this is a hexdump of a 90x90 jpg picture, 24bit */
    private static final String JPG_HEX_DUMP = "FFD8FFE000104A46494600010101006000600000FFFE003C43524541544F523A2067642D6A7065672076312E3020287573696E6720494A47204A50454720763830292C207175616C697479203D203130300AFFDB00430001010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101FFDB00430101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101FFC0001108005A005A03012200021101031101FFC4001F0000010501010101010100000000000000000102030405060708090A0BFFC400B5100002010303020403050504040000017D01020300041105122131410613516107227114328191A1082342B1C11552D1F02433627282090A161718191A25262728292A3435363738393A434445464748494A535455565758595A636465666768696A737475767778797A838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE1E2E3E4E5E6E7E8E9EAF1F2F3F4F5F6F7F8F9FAFFC4001F0100030101010101010101010000000000000102030405060708090A0BFFC400B51100020102040403040705040400010277000102031104052131061241510761711322328108144291A1B1C109233352F0156272D10A162434E125F11718191A262728292A35363738393A434445464748494A535455565758595A636465666768696A737475767778797A82838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE2E3E4E5E6E7E8E9EAF2F3F4F5F6F7F8F9FAFFDA000C03010002110311003F00FEFC2992B88A3791880A88CEC49E00504939EDC7AFE24767D64EBD657BA9689ABE9FA6EA32691A8DF69B7D6961AAC56F6D772E9979736D2436D7F1DADE47359DCBD9CCE970B05D432DB4A6311CF1BC4CCA4FEB4B7EB65F789ECEDBDB4EBF96BF71FC92FF00C15F3F69793C61F1D27F87F65FB43780B57F856DA578BE21E1CF046A293F8FFE04F8DBE15DA691E0DF8C8BE2CB8D0BC1DE253A7681E23F01FC4AF15E8FE3CD57C61FF094E91A6787135AB4D1BC07FF00099785EC0EBDC6FC56FD943E1E4DFB31C3FB49DEFED41E22F8B9E25F8A3F11FC23E33B8F8AFE0EF851E2AF1478D3E2DFC5A3AE43E0EF0947A5FC20B1F8A3E1AF01FC4AD23C2B30F1058D9786B5FF00867E2CD4A3F0245AD693A0A5BF871EE34E9FEA0F8A3FF04EB3F0BBE345B78BBE2FF8BB40BAF1AFED73E05F8C9F06757F885E06F02DD787BC39A5FC45964F02FC4BF0B6AABE12D57C61AF5BDAF887E27EB3A47C72F1D78DF4BD0B57F0CF867C49ACEB97DA4695A5E9F7B39D5356E626F00FC02F0CF856EFF65FF8A7FB43E8D7979F0F7E205F7C4AD27C79E04F881E11F107C72D73E35788349BFF0009F88BC11A3FECCDA8787BE29F892CE35D075CF1047A936BEDE32D7F58D7757B6F178D56F3C41AAEA77FA4FE79C4198E2A199CA94AAD6C2C21528B54A8FEF79F0D3824EA4673724E74EFC9082768734A2A4A3147DE64B814F2AA35F0F86C3E3655286292C4626A2C2C5E3A8CA8CA186AB4E9D09D5A746B39A9D6C43A73768427EC2ACE765F0D7ECADFB59FED2BF02BF680F84DA86AFA77C43D434AFDA43F694F137C4FF891E27F0968B2DC7897F68D93C5D15AF82BC27656FF000B353F03F86EC3C23A27876FFC27F1B66D52D6CB56B2D534FB7F0F786E7D56FEE747D03E1AC1E3AFEE2A193CD86290823CC447C74C6E0090783EBF87BD7F3F5A67EC95A5FC5DFDA23E0B7C36F865E31F1BFC35BEFD8C3E027C41D61BE26F85EFB47D47C6FA1F8B7F687F12780F46F0CD9EB9A878BB41D7B49D4BC4FE30F047827E37EAFE2C8F53D21B53D3E5F13693E23B68B49B9D5B4AB87FDFCD3ADA4B3B0B2B496692E24B6B582079E560D24CF146A8D2C8C15433B952CEC15416248551803EA787EBE2315964311889CA4E75AAAA7CD6E674E32E5E77ADF9A6E2E53F7541CE529465294A6A3F3BC41428E17359E1E84294634A852F68E8CB9E9BA925CE92A89724E3084A3085A4E518455370A70853E6BC3FCF207F3A767BF6C838C8EBDB1E9D79E3031F806F1F8FD38FE7FD28E3BF5EC31C76EA7AFF009CE7AD7B478C2F7F40793C827BF7F5E7A75A5F6181C1079072323A91FE19EDCF66E7A7D0F61D79EB9EBDBFA5191DF81D7803F219E9400A4E718E38C7DE078C7A76EBD7A7E347CBDF767BE318FC293B1FC3B7EB9EDFFD7A33E98FC97FC280128A2B83F899F127C25F08FC13AF78FF00C6DA97F66F87FC3F666E6E1A389EEAFAF6E247482C348D22C22CDCEA9AE6B17D2DBE97A2E9368925E6A9AA5DDA585A4525C5C468C9B4936DD924DB6F6496ADBF41A4DB4926DB6924B76DE892F36CF8C7FE0A31F14BE11F85FE01F8C7C11F107C29AC7C40F1278A7C33E29D67C09E0FF0CC8B67E24B3D57E1EE8D378C25F89D07883FB4F447F01E8FF0A67B1D3FC59A87C40FEDAD266D02F20D2EDF45BAB9F12EA7A2697A87F36A9FF05465F0241F087C1DADFC77F8C7326ABE04D4E4F8ADF16FFE19E7E17789FE27683AE69A2DEC34AB5D075CB416DE16F15D9EABA9597886CAC3C450FC37F12B5BD95BE8DAA6BBFDA9FDA975A847FAAFFF00050CD5FC69F0C3F603FDA63F69FF001EE9AFE1AFDA17F68BD1FC1BF08BC3BA44EF14BAEFC21F86DE3AF14E99A2695F0AECAFA0BDBAB05D5340F0D6A3E24F1978F351D22E23D3B53F1ACDAADCF9F75A1E87A2CB0FF255AF6893EA3E1CD6B40BD7867F1A697E3AD7352B3B0DB6CBA8C725C2241E24D249B5668B5093528AD745D674E4B3796C6CE6D2F5CB7B23F6AD5DE17F99CD274255E0EB607098B92853A94E38AA3293A708D57250BC6A529FEF9424A74D37752514A325CEBFB03E8D5F47ECA3C5DCBF3ECCB88389B89B8772DCB732A39350ABC338BC050AD88C46332FAEF198BAF1C7E5998D1587CA2B57CA2BAC42742308CEAFB5A9255695397F64DFF0449FDA53E157C6DFD9C2EB4CD234BF11683F18EDBC49E2CF117C49D43E214B6F71F107E32C7FF097EB7E0DD3FE36EA7AB477778BAF0D67FE112FF845F5782C675D33C0DACF875FC19A569FA4F85AC7C2715CFED657F2C7FF000444F85FA77ED01FB1A78DF48D2FC457FF000DFE36FECF5FB4778BF57F82DF17342B185F59F05DBF8F7C03F0F75EBED3AFB4CB82965E2DF87DE2CD7D7C41A6F8EFC0FAA7FC4A3C530590BBF334EF11E9DA2EBDA57EF97ECFFF00B416A9E37D5FC41F073E30E8563F0FBF68DF875676D75E30F08DA5CCF3F877C5FE1CB99FEC7A57C55F859A85F08EEF5FF877E23B856842CC8759F076B6B73E13F14C516A76B05DEA5EB65D594B0B864E318295351A7C91E4A7EEAB3828D9284A366B976925CD1EAA3FCEBE2370CC78478EF8CB8629D5AF5E3C39C499CE4EAA62A4A78AAB472FC7D7C2D1C4D694614E3375E9429D4A928D3828D49B8F2462E0E5F55528C77F5FD2A0FB4402716DE745F682865106F5F34C60E0C823CEED809C6EC6DCF19CD4D5E89F101FD3B1FE5FE7145145002FFF005BD7F1FD73FD28C8F41FAFF8D251401F17FED8FF00B52B7ECE1E19D1EE7C332E85AF7C419E4B9F1345F0DF53F0EFC46D76F3C5FE02F0E7943C652D9EA9F0DBC35E2FBCF87E34C4BFD3EE3FE13FF15787751F04693285B2F100B1B6D40EB3A57857ECB1F13FC2DFF050BF1447FB4B4B7BB3E177C2FD725D1FE10FC19D61AD20F157877C656B04906B5F157E2C787ADF53BD3A7F896FA2B89F4EF853A46A1676B2E87E1192F7C651CB7B73E37B05D07EF1F8B7F08FC33F19FC1FAB780FC622E25F0C7881F468FC41656AF0C6DAD68FA5EBBA66B77BE19D4BCEB7B88AEFC39E268B4D6D07C4FA6BC606A9E1FD4B53D38C910B9F3538DF815FB347C36F80716B33F8574BB4B8F116B9AE7C43D42FBC5B71A5E9769E229F42F1E7C5BF1D7C5DB5F06DCDFE9B6968F79E1BF04EAFE3ED5746F085ADD0964D3B4482DE2677B992EA79F99D3ACEBA6E51787B5DC2CD3E78DB95BD5DECEF2E91768DD5E377D319D18D0694651C473594EF74E12DD2565CAF4B5FE249BD5A95A3F865FF000717FC4A82C7C01FB347C168753FECE93C51E2FF001F7C4CBE551E65BBDA780BC271F83B4FB6D52148A56361757FF149AE909411B4FA501215B65BA9A0FE58756F0EF8934FD12DFE22B5BCB65A85DF89AE534F7D3A1585B4E5F0E436CF0EAF6D2C37574F736B7572BA942270AD1C373A0493CDA83CB22C727EFDFF00C142AE3C17FB587FC1717E187ECF3E3BBCF105C7C37F861F0E749F87FA9E97E1EF10EA5A0B0F883A87C32F1D7C7486F65BCD32589F79B5D7FC0503C1233C522D8BDBDCC52C733C6BF56DF7FC11F3F649BDB1B7D3C5E7C668B4DD3CAC16B64BF15B5696DC45F6CBCB88EC8437B67748D69E6DCEA13B40EBE59FB6DFB3AB2DFCEAD961B85B34E239E3319809617D951C53C146356BCA94E35A8D3A1EF59539C791BA968BBA7772D15933FB03C37FA617007D15384B86B8238BB8538B3199BF14507C7D8CC665781CAB1582C6E439E57C6E5F85C3D278ACCB01898D5952C8F075AA39C6AD2F6718A8DA352719F82FFC1BCFF1474BB0FDA27F6BBF854B736312FC52F02FC39F8E1E1BD274D8FECDA7D8CDE18F11789B40F1A45656CC331C0967F117E1DC114431F678ED0A0DE857CBFDE5FDBCFC23E1387E07F8A7E3B4D7BAEF84FE28FECF1E1BF137C45F84BF10FC169683C67A2789ACF4A91D3C316D15EC5369DE20F0CFC40B882C3C33E2AF066BB05DE83E23B4B8B6373041A8E9FA56A9A6FF003B1F0B3E157C39FD81FF00E0B2FF00B25F827E156A9E2DD3BC23F103E167873C23E319BC57ACDD789DB5093E2845FB44E896BA4C170E2D6CB498358F17FC37F85370D043691CB79AAE97F6B8E4426E6CEFBFAFABDB1B1D4AD64B3D42D2DAFACE75D935ADE4115CDB4C87076CB04E8F148B900ED74619038A587C356C3C3179762796388C1E26B61EA724B9E2A6A4A6A50768BE55295E0ED1768A6AD656FC33C5CE28C938E78C61E2570CE1331C1F0EF8819665FC4D9561B34853A7982A518D4C931CF14A8CAA50F695F32C9F1F524A9D4AA92A9C9524EA29A3F02BFE0953F14BF6A8F89FF14FC7BE22FDA56DFE1D5B7C77D6350F15D87C57B5D46C3C7361AF69DE08F0078A7C57E0DD2FC35F032E535BD7FE1DFF00C207E03F8836D2681E2BF0DADB689E2DB5D5AFA6F12F8B26D6A6F10F8575FD7FFA00AE53C37E07F0978426D6EE3C35A0699A34FE24D7B54F146B93595B47149A978835AFB2FF006BEAD72E06E6BCD48D95A35EC808FB43DBC4F206750D5D5D7461A94A8D2509CDD4926DB94B56EF6DDF56DDDB7D5B6CFCC3135A35EACAA469AA71692508ED151564925B24AD14BA24828FF3D31FE7FC8A28ADCC028A28A0029188452CC40503249C1C7D40E7EB4EF7F73D87B7E9D38FAFE3E1FF00B4BFC465F845FB3D7C6CF89C188B9F037C2FF1B78974D8D582C975AC697E1ED42E746B184B023ED17FAAA59D95B8EF34E8306A652518CA4F4518B93F44AEFF000454212A938422AF29CA308A5BB949A497CDB3F829F17FC739750FF8282FC40FDA72C0EA57FA8CBFB47F88FE2D44D6371A525F5DF80341F1F5C5A787741B0D6F52D4F44B1D0A3F127C11D234DD01AF7EDD772DCD8EAB1D888E417B73A527F639F0FF00C73E0DF8A5E0BF0CF8EFE1EF88B4DF167837C5D616DAEE85AEE8F711DD59EA9A75D2C820786443FB996154920D42094C73D95E24B6779141716F344BFE7ED6BA4DEE8363068D732ADCC7A646B65AA6A30CCEF6DA9CBA6C102CCB6339C3CF673136A90DD2A859D6EADA44090CB9936EC7C45E23D62E3FB1D35FD7347F0EEA4F02EBDE1ED07C43ACF87BC39ADAADB5BD9CEFACE87A4DFD9E97AC5E6A105B470DC4FA859DDCF3BB3492B392E4F99C35C5F5B87BEBB1786789A58C9C6B3A5ED7D97B3C4454D2AA9FB3A9A352B4D24A4DA8CA328B8EBFE96FD233F67F54F1CF26F0A31DC3DC599670C66FC11C1394F0A66F5B32C057C5E1B1F95E0B0D869A952FAA4D5658EA75DE29528558CA9CA94A34EB55E69DE3FA83FF00050BFDA37C3BAB7EDFBA47C6EF877E278F59D2BF671F127C15B48357D26E6E2EB4AD447C16F1C45E3ED62F23B9B481EC2F34F8BC53E36F13F8360D663BC96D3EDFA2EA51C25AD2F6CE6BEFEF2B4EBEB5D534FB1D4AC665B8B2BFB4B7BCB49D09D935B5CC493412A13D5648DD5D49EA0835FE64D757DA7F88AE63B69FFB3345D17C477369E0F0561D3AD2D7C39E16D17C3D6FE13F0C476D24B13DFC5A541A1CFA9C7AE5B4574961AA4B6768752B86BAB50CBFE82FFF0004EDF8923E2DFEC39FB2D78E5AFC6A9797FF00067C19A4EAFA802375D6BFE13D353C25E20925019F6CC75BD0F50F390B31597702C7AD460B30A98FCC332C4D5945D4C654789946374A32E792928A6EF6519C37BB7D5DEE7E23F498F06707E0DF047813C3597CAB62A8F0FF0009E67C2F89CC2AC529E271786CC7FB66B4AA2843D9D272CC738CDFEAF4555A9258485295DA6A4FECEA28AF8C7F6E9FDADEE7F628F82F6DF1C66F85FAA7C51F0B69FE32D0741F1BD9E8BAE268BAA7863C3BAE25F5B43E25B28A4D1F568F5CBB7F10AE85E19D3F4291F488AF753F11D8BDCEB7A659C17372BE862F1787C0E16BE33155152C361694EB57AAD4A4A9D2A69CA737182949A8A4DB518B7D933F9170D86AF8CC450C261A9BAB88C4D5851A14938C5D4AB524A308273718A72935157695DAD4FB3A8AFE7A35AFF82CB7C58D425FF848FE1E7C0CF8517DE1AD505F58784BC11E2CF8DB6BA67C47F10EB9A7D9B4B79A5B6A5E1DD13C49E09B2D72D2EE0BA866F0D59EA7AC88628164BFF1169CE6E12D7F52BFE09FBF1E3C7DFB4C7ECA3F0E7E34FC4EB6D16CBC67E2ED57E2641A9D9E81A26A1E1AD3EDAD7C2DF15FC71E0ED1A23E1FD5B55D6F54D1EF868BE1FD3FFB534FD4354BBBBB7D4FED693989F74117C4F08F89FC0DC778FCCB2DE14CFA86718BCA3DA3CC29D0A38984684615E387553DA56A34E9D5A552B39D3A35A94A74AB4E862234E72961EB287D6712787DC5FC2182C0E61C4793D5CAF0B993A6B073AD5B0D3956954A2EBF27B3A35AA4E9D4A74B967569D450A94955A2E714AAD372FB368AFC97FF0082A8FED17F16BE0EE93F003C0BF01FC4F3785BE277C4DF89693DB5DDAD9E9BA84DA9697E1CBDF0F689A778567B4D4ED2FA03A7F8CBE2278EFE1F787B5775856E5B41BBD5E1B3B8B4BB78AEA0FD5F805D18613281E698A3326178F30A02F8CE0E37671900E3A815F5585CDF0B8CCCB35CAE8AA92C464EF02B192E47EC633CC283C4D1A51A96B4AB4687255AB4D6B0A75A849BB5556F9CC4E5989C2E032DCC6B722A39A7D71E162A57AAE182AD0A152ACA16F7694EB39D2A53BFBF3A15959725DDA032719E80FE9CF7C7F9E6BF0E3FE0BF1F1FEDFE12FEC5567F0F34CF10A68BE38F8EDF14FC05E14D06389DBFB44683E11D6EDBE23F8B755B78A35690DA59597852CF4DBC971E58935BB5B77DFF006911B7EE3FF2FAF51D71FE7DB8E99FE7E3FE0BD9FB277C75FDA4BC11FB3A6B7F02FE16B7C51D43E1CF8C7C7D2F8B34DD37FB060F1069DA1EB7E0D37106A7657BAEEA7A2C42C629B40B8B1BAB28F53824BDBED534B48229EE9204AE8CC3DA7D4B11ECA3295470E5518A9395A5251934A3AB6A2E4FCAD7E87DAF8474B87AB789BC0CB8B3134709C394F8932CC4E7188C4CA9C70D4F0584C4471557EB2EA4670FABC951E4AC9C24E54E528C5734935FC9AEB7E1DB7B0F090BFD6637B28AFF00CB7D0ACAF27336A361A63C53DDD8DB5FCC1219DEEAFEEA57BFBA2616416D04A2DE38A185636F11B2B0BB9EFBEC91068E744924DDC90BB232E8CA5721849B90452292AC245756DA4357DDBE39FD9C7F699FF843F50F147C60F817F1BFE12681E1CD1ACFEC0FA9FC13F8A5E20B6BAB6BF8ECF4CD16C1753F0DF85757D1EDEE5AEEEA0B1D3D755D66D82FF6C5FDB075BA36B6B2E7DCFEC97F197C1B78DA85FF00C26F8EE96313D847A7C12FECD9FB425B1B96B5D17C4BAEDE5B4975A8FC2BB3B31656BAB784F487BA9A5B84115A78822D47220B6D6921F8DA9424E4B929CA31B417BE9464AEACE5387C51BB4F74BB474B23FDB4C97C75F0C70981C553C6788DC215B31954C454787A39EE0EB61E9B8FB254B0F42BFB5951AD53D94E0EB4955A979F3CF1128548D4E6F91749B7645796F74B87531069F1DBD8DBDC5C4F6A6DA7BEBEB96B6B84113C61A6CFDB2F962BA12C06D8095ED9EDB32C5FDB77FC1053C446E3F6185F8792EA30EA53FC25F8A9E32F0F09A09EDEE234B1F1BDA687F192C6147B6664F2AD97E254B631210924496822995678E503F91EF1BF811FC23E1CF83FA5B6A8A9A478D974AD73C43E33BC8FC4FA4E85A45BCD34F1C735D59EBBA5699630E887C3A741BBB2D4237B81AB1D105E25CCB6FAA59429FD1E7FC1BB7A57C42D274CFDB1E4D5BC1DE25D03E17EBBF10BE1E6B5F0F75ED4F42D4F44F0EF88B598B46F14687E2B8FC2726A16D6B1EB363A568BA2F81126D534F49ACD85E5BDB1BA7BA82E6287D2CA632A58D846D7E684A12B5BDDE6A7ED5F35AF78DE308F35DAE669277BC57F28FD33B8A384F8F7C2ACAF34C9389322C756CAF8B29E2F0D85C1E6B85C5D7C560E6B1D92D5A987A10C4BA94DCB11ED3153A71C3C2A3C3469D7C4C6CE9543FA52AFCB7FF82AE7C05F8F9FB427C03B6F017ECEF3F884F8DB58D49ECDAC6CF50D2AD3C2328B69F4DF10E917FE3B8F5EF1AF86B4B4D0F4ED7BC3FA6B36A11787BE216AF642698699E12BC6B89DABF5228AFA4C5E168E3B0B88C1E260AA61F154A742BD369353A5522E1520D34D3538B7169A69A6F43FCBCC2E26AE0F134317425CB5F0D5615A8CB5F76A53929425A34EF1924D59AB348FF3AEF137C38F8ADF0A1BC47E03F147826E7C07F19FE1B68DE17D4FC2FE18D534CF885AA78E2C7C55A75A78422D4A2BDF0CE8FE0FD5F4AF14F85AE3FE105D22F7E1C789F4A9B51F0B37FC22B6B79AE6B5E20B4BBD53506FEA13FE093DE2AF847FB317EC88FA87C71FDAB3E09DCF8ABC6BE31F1278EBC557977F1F3C0BACF873C3F73ADEA179A90D2F4FB95F1EEAFA369922ADD4B75AD5A585AF87CFF6FCFAA79FA52ED8247F913F6F7F88BA0E89FB6A7C7B9F527D4596CE0F859E139DB4CD2B52D664B3B3F0FF00C2A87C7DADEA9A845A659DCCBA6787BC3DA47882E757D7F59BC11D869B6A866BCB88D1ADD1FE5D935CF068BC1AEC9E1DBD6D4A4965B6F0F6A6FE0BBE6D47C633DBF8CAEFE1D4D6BE03BBFECF6BBF16CD178FACA5F05B43A1BDC4A3C42F6B63B36DF584D75FE72D5F1733DF073C49F10B0594F865C45E2060F058EFECBCBB892A63F16AAD2A7531D9752C560A9E2219263955A35F3FCDB0D4ABD1A15D279BE27DAFB2FAC62EA27FDC51F0DB25F13780F82F118DE37C87823118EC253CCF1D91E1B2FC22A556AD3C0621E17153A2B33C1BA7530F9265955D0A9569BE5CAF0D1A0AA2A185825F787C49F8C1A0FED73FF0506FD9DFC53E157B9BFF0086DE1DF8ABF0FF00C11F0A354B8B4BCB283C516BE0DD4751F8C9F113C79A5DBDFA5ADC1F0F789756F02E91E1FD1EF26B489754B3F87F17886C5AF345D7347BC93FA3AAFE68FF00E09FD6DA67C49FDB07E0A5FDA2CB71A57857E1F7C56F8BB6CD75693DA5D5A5F5B693A1FC32B6B5BBB1BD8E1BBD3EEE25F8ABA9ADE58DD4115ED95FD87D9AE6286782545FE972BFA8FE8E7C459EF1AF05E73C75C4782596E69C5DC639DE3A79628D487F6650CAA183E19A197355A30ACE584591CA9549558C673AAA739429CA52A71FE7AF1BF24CA78578A32AE11C9714F1D97F0DF0C65585863DCA9CBEBF57319E2B3DAB8D4E94A54F9713FDAB1A9154DCA118B8C633A914A725E7A7A67FFAFF00CBF4A8DDD100DECAA0F0371033F9F5A917AFE07F91AF3CF88F737367E02F1ADE5A5C4F6B776DA36A325BDD5BCB2417303C76ECD1BC33C4CB2C4E8C0323232952320835FBF9F8C4E5C9094AD7E58CA56DAFCA9BB5F5B5EDD8F97BF6FBF10E810FC18F09F822FB5AD2ACB58F89FF00B42FECBDE0AF0F69975A95ADADFEB93DE7ED15F0C2F755B4D2AD269526D4A6B7D06CB53BEBBB7B6499E2B0B7B9B99905BC523AFD41F163528743F853F12757B96021D27E1FF8C35095881F72C7C3BA8DC39038FE18CE075AFE47BE0E6AFAB7C49F157C1FF1CFC44D5351F1F78DB40FDAEBE00E99A178C3C697B73E29F14E8BA6CDFB41783ADE6D3F49F106B92DF6ADA6D94B0224125A59DDC30490A2C6D19450A3FADCF8C6AAFF00087E2A23AABA3FC37F1C2BA300CACADE18D501565208652090410411C1ADB1B82FAA3A53F6BED1E2F014F12972727B3B57C651E4BF3CB9F5A2E7CD687C5CBCBEEF34B8729CD7FB4E38C87D5FD87F6766F57017F6BED7DB3FA96598BF6D6F674FD9698A54FD9DEA7F0F9F9FDFE48F8AFEC8961E0FF187EC5FFB386872B681E34F0DC9FB3F7C26D0F52849B0D7346BE7B1F01E81697F6372ABF69B2B9105D4124173036F5596278E4505596BEB100280140000C0006000381FA57F28DFF06D5788BC40D7FF00B4C7815B5DD64F827C35A0FC1BD4BC39E0E3A9DE9F0AE81A8EBDE1A5BAD72FF45F0F19FF00B234ABDD66E556E355BAB1B3827D467512DE493480357F575558CC22C0E2ABE0D4FDA2C34951551C545CD463177714E56BDF64DA0CA733A99BE5983CC270F63F5B84B11EC233738529D4972CF95B51BB92A704E5CA9B518A7A450514515CE7A07F2FBF14BC37AFFC57F8EFFF00057DF8916DA3DDDCD9FC2CF843F143E1F99E3B2B899ED2F3C53E0FF859E13BEB4B710412B5D6A369F0F3E106BDE2892DE28A4BEB7D23C63A53A036DAC4265E92FBE1178C26B9FF00822AE850783BC4774BAA7C3CF87FE23F17DE5BE8FA85CD868FE2483E247C06F8F9E344F12DFDBDABD9E97728347F136A6AFA9CD6C6EA7B4BE8AD5E6BA2F1B7F4AD1DB5B466764B7811AE5D9EE0A451A9B87DA23DF390A0CADE5A247BA4DC7622AE76A8026088A142A2A8418401400A3A614018518EC302BF3CFF00887D95D4A78884F1389954C4E70B37A95B968F33ABFEB9478CDD249C1AF672C551C3E066DDE4F0987A4D38D48F31F70B8E7308CF0EE185A10861B2CFECCA54D4EA72AA7FEAABE16551B4D3738D19D7C6A5B7D66BD48BBC19F8A9FB0C7C03F881F0A3F6CEFDA475AF10F8735AD2FC27E1F9FE37E99E1AD4EF348BDB1D0357D3BE347ED033FC55F09C7E18D4A6812CB5A8F4BF03E95A1D9EB274D9EE23D235153A6DD25BDC290DFB285EE9C9609200C4B000F001E40FC338A8B00DC8C80732E0E79C8CAF073D47B56F57D3F0EF0FE038632D9657962A91C2CB32CEB34E5AB2E7947119EE6F8ECEB1918CACAD4D63330AEA8C6DEE525085DF2DDFCF6799DE378831EB31CC1C1E256072BCBDBA71E48BA394E5B84CAF0D26AEFDF961F074A5565F6AA394ACAF63FFD9";

    private ConverterJpgFromHexDump myClassUnderTest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        myClassUnderTest = new ConverterJpgFromHexDump();
    }


    @Test
    public void testLoadImage() throws Throwable
    {
        BufferedImage actual = myClassUnderTest.convert( JPG_HEX_DUMP );
        myLog.debug( actual );
        Assert.assertNotNull( actual );
    }

    @Test
    public void testResizeImage() throws Throwable
    {
        BufferedImage in = myClassUnderTest.convert( JPG_HEX_DUMP );
        BufferedImage actual = ConverterImage.resizeImage( in, 200, 200 );
        myLog.debug( actual );
        Assert.assertNotNull( actual );

        File out = new File( "resized.jpg" );
        ImageIO.write( actual, "JPG", out );
    }



}
