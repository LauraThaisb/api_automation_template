package br.com.sicredi.api;

import br.com.sicredi.design.BaseTest;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SimulacoesTest extends BaseTest {

    private static final String CPF_SEM_SIMULACAO = "02886342092";
    private static final String CPF_COM_SIMULACAO = "17822386034";

    @Test(groups = { "health_check" })
    public void healthCheck() {
        when().
                get("/api/v1/simulacoes").
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test(groups = { "contract" })
    public void contrato() {
        given().
                pathParam("cpf", CPF_COM_SIMULACAO).
                when().
                get("/api/v1/simulacoes/{cpf}").
                then().
                body(matchesJsonSchemaInClasspath("schemas/simulacoes_schema.json"));
    }

    @Test(groups = { "functional" })
    public void simulacaoExistente() {
        given().
                pathParam("cpf", CPF_COM_SIMULACAO).
                when().
                get("/api/v1/simulacoes/{cpf}").
                then().
                statusCode(HttpStatus.SC_OK).
                body(
                        "nome", equalTo("Deltrano"),
                        "cpf", equalTo("17822386034"),
                        "email", equalTo("deltrano@gmail.com"),
                        "parcelas", equalTo(5),
                        "seguro", equalTo(false)
                );
    }

    @Test(groups = { "functional" })
    public void simulacaoNaoExistente() {
        given().
                pathParam("cpf", CPF_SEM_SIMULACAO).
                when().
                get("/api/v1/simulacoes/{cpf}").
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

}