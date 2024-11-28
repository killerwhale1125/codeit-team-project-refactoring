package com.gathering.common.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

// JPA 테이블 및 컬럼 생성시 스네이크 케이스 및 대문자 변환
public class UpperCaseNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return convertToUpperSnakeCase(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return convertToUpperSnakeCase(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return convertToUpperSnakeCase(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return convertToUpperSnakeCase(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return convertToUpperSnakeCase(name);
    }

    private Identifier convertToUpperSnakeCase(Identifier name) {
        if (name == null) {
            return null;
        }
        String snakeCase = name.getText()
                .replaceAll("([a-z])([A-Z])", "$1_$2") // 카멜 케이스 → 스네이크 케이스
                .toUpperCase(); // 대문자로 변환
        return Identifier.toIdentifier(snakeCase);
    }
}