package net.coru.kloadgen.config.avroserialized;

import static net.coru.kloadgen.util.ProducerKeysHelper.SAMPLE_ENTITY;
import static net.coru.kloadgen.util.ProducerKeysHelper.SCHEMA_REGISTRY_URL;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.coru.kloadgen.loadgen.BaseLoadGenerator;
import net.coru.kloadgen.loadgen.impl.AvroLoadGenerator;
import net.coru.kloadgen.model.FieldValueMapping;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AvroSerializedConfigElement extends ConfigTestElement implements TestBean, LoopIterationListener {

  private String avroSubject;

  private String schemaRegistryUrl;

  private List<FieldValueMapping> schemaProperties;

  private BaseLoadGenerator generator;

  @Override
  public void iterationStart(LoopIterationEvent loopIterationEvent) {

    try {
      if (generator ==  null) {
        generator = new AvroLoadGenerator(schemaRegistryUrl, avroSubject, schemaProperties);
      }

      JMeterVariables variables = JMeterContextService.getContext().getVariables();
      variables.put(SCHEMA_REGISTRY_URL, schemaRegistryUrl);
      variables.putObject(SAMPLE_ENTITY, generator.nextMessage());
    } catch (Exception e) {
      log.error("Failed to create AvroLoadGenerator instance", e);
    }
  }

}
