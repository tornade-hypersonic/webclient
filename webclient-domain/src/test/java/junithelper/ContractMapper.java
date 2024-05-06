package junithelper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContractMapper {
    ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

//    @Mapping(source = "telNo", target = "telNo")
//    @Mapping(source = "bunkatsuNo", target = "bunkatsuNo")
    // 他のフィールドについても同様にマッピングを追加する
//    ContractDto mapContract(Map<String, Object> contract);
}
