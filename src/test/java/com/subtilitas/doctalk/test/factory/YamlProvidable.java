package com.subtilitas.doctalk.test.factory;

import com.subtilitas.doctalk.test.factory.impl.YamlFactory;
import com.subtilitas.doctalk.test.factory.impl.Yamls;
import org.yaml.snakeyaml.Yaml;

public interface YamlProvidable {

    static Yaml yaml(Yamls yaml) {
        return ((YamlFactory) FactoryProvider.getFactory(Factory.YAMLS)).create(yaml);
    }
}
