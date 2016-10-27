package uet.jcia.utils;

import java.util.List;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;

public class HibernateHelper {

	private static Configuration hibernateConfiguration;
	private static final String MYSQL_DIALECT = "org.hibernate.dialect.MySQL5Dialect";
	
	public static String generateDdl(List<String> hbmList) {
		createConfiguration(hbmList);
		Dialect dialect = Dialect.getDialect(
				hibernateConfiguration.getProperties());
		String[] ddl = hibernateConfiguration.generateSchemaCreationScript(dialect);
		Formatter formater = FormatStyle.DDL.getFormatter();
		StringBuilder res = new StringBuilder();
		for (String s : ddl) {
			res.append(formater.format(s)).append(";");
		}
		return res.toString();
	}
	
	private static void createConfiguration(List<String> hbmList) {
		hibernateConfiguration = new Configuration();
		for (String hbm : hbmList) {
			hibernateConfiguration.addFile(hbm);
		}
		hibernateConfiguration.setProperty(AvailableSettings.DIALECT, MYSQL_DIALECT);
	}
}
