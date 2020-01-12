package com.revenat.germes.persistence.schema;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

/**
 * Dynamically generates SQL database schema
 * @author Vitaliy Dragun
 */
public class DBSchemaExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBSchemaExporter.class);

    /**
     * Creates file with DDL statements to create project database from scratch
     * using specified dialect and annotated entity classes
     * @param folderPath path to folder where schema script will be created
     * @param dialect hibernate dialect class for
     */
    public static void exportDatabaseSchema(final String folderPath,
                                            final Class<? extends Dialect> dialect,
                                            final Collection<Class<?>> entityClasses) {
        final Metadata metadata = buildMetadata(dialect, entityClasses);
        final SchemaExport schema = new SchemaExport();
        schema.setDelimiter(";");
        final String schemaScriptFile = folderPath + "schema_" + dialect.getSimpleName() + ".sql";
        deleteSchemaFileIfExists(schemaScriptFile);
        schema.setOutputFile(schemaScriptFile);
        schema.create(EnumSet.of(TargetType.SCRIPT), metadata);
    }

    private static void deleteSchemaFileIfExists(final String schemaFile) {
        if (Files.exists(Paths.get(schemaFile))) {
            try {
                Files.delete(Paths.get(schemaFile));
            } catch (final IOException e) {
                LOGGER.warn("Can not delete db schema file:", e);
            }
        }
    }

    private static Metadata buildMetadata(final Class<? extends Dialect> dialect, final Collection<Class<?>> entityClasses) {
        final StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", dialect.getName())
                .build();
        final MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        entityClasses.forEach(metadataSources::addAnnotatedClass);
        return metadataSources.buildMetadata();
    }

    public static void main(final String[] args) {
        final Reflections reflections = new Reflections("com.revenat.germes.application.model.entity");
        final Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
        exportDatabaseSchema(
                "",
                /*MySQL57Dialect.class*/PostgreSQL95Dialect.class,
                entityClasses);
    }
}
