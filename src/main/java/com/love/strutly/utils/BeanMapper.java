package com.love.strutly.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import com.github.dozermapper.core.loader.api.TypeMappingOption;
import com.github.dozermapper.core.loader.api.TypeMappingOptions;
import com.vip.vjtools.vjkit.collection.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class BeanMapper {
    private static Mapper mapper;

    public BeanMapper() {
    }

    @Autowired
    public void setMapper(Mapper mapper) {
        BeanMapper.mapper = mapper;
    }

    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    public static <S, D> void mapExcludeNull(S source, D destination) {
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                this.mapping(source.getClass(), destination.getClass(), new TypeMappingOption[]{TypeMappingOptions.mapNull(false)});
            }
        };
        mapper = DozerBeanMapperBuilder.create().withMappingBuilder(builder).build();
        mapper.map(source, destination);
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        List<D> destionationList = new ArrayList();
        Iterator var3 = sourceList.iterator();

        while(var3.hasNext()) {
            S source = (S) var3.next();
            if (source != null) {
                destionationList.add(mapper.map(source, destinationClass));
            }
        }

        return destionationList;
    }

    public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
        D[] destinationArray = ArrayUtil.newArray(destinationClass, sourceArray.length);
        int i = 0;
        Object[] var4 = sourceArray;
        int var5 = sourceArray.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            S source = (S) var4[var6];
            if (source != null) {
                destinationArray[i] = mapper.map(sourceArray[i], destinationClass);
                ++i;
            }
        }

        return destinationArray;
    }
}
