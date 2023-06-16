
<h1 align="center">App Denúncia</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" border="0" alt="API"></a>
  <br>
  <a href="https://wa.me/+5511961422254"><img alt="WhatsApp" src="https://img.shields.io/badge/WhatsApp-25D366?style=for-the-badge&logo=whatsapp&logoColor=white"/></a>
  <a href="https://www.linkedin.com/in/rubens-francisco-125529162/"><img alt="Linkedin" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"/></a>
  <a href="mailto:rubens_assis@outlook.com.br"><img alt="Outlook" src="https://img.shields.io/badge/Microsoft_Outlook-0078D4?style=for-the-badge&logo=microsoft-outlook&logoColor=white"/></a>
</p>

<p align="center">  

⭐ Esse é um projeto para demonstrar meu conhecimento técnico no desenvolvimento Android nativo com Kotlin. Mais informações técnicas abaixo.

:exclamation: Aplicativo que provê uma rede social onde você pode se cadastrar e fazer denúncias sobre problemas que você encontra na sua cidade. Você pode postar denúncias, ver as denúncias de outros usuários,
curtir denúncias que você achou relevantes e comentar essas denúncias.

</p>

</br>

<p float="left" align="center">

<img alt="screenshot" width="30%" src="https://github.com/rubens23/appDenuncia/raw/master/app/src/main/appscreenshots/Screenshot_20230616_194322.png"/>
  <img alt="screenshot" width="30%" src="https://github.com/rubens23/appDenuncia/raw/master/app/src/main/appscreenshots/Screenshot_20230616_194432.png"/>
  <img alt="screenshot" width="30%" src="https://github.com/rubens23/appDenuncia/raw/master/app/src/main/appscreenshots/Screenshot_20230616_194458.png"/>
<img alt="screenshot" width="30%" src="https://github.com/rubens23/appDenuncia/raw/master/app/src/main/appscreenshots/Screenshot_20230616_194549.png"/>
<img alt="screenshot" width="30%" src="https://github.com/rubens23/appDenuncia/raw/master/app/src/main/appscreenshots/Screenshot_20230616_194628.png"/>
<img alt="screenshot" width="30%" src="https://github.com/rubens23/appDenuncia/raw/master/app/src/main/appscreenshots/Screenshot_20230616_195725.png"/>
  


</p>

## Download

Faça o download da <a href="apk/app-debug.apk?raw=true">APK diretamente</a>. Você pode ver <a href="https://www.google.com/search?q=como+instalar+um+apk+no+android">aqui</a> como instalar uma APK no seu aparelho android.

  


## Tecnologias usadas e bibliotecas de código aberto

- Minimum SDK level 21
- [Linguagem Kotlin](https://kotlinlang.org/)

- Componentes da SDK do android que foram utilizados:
  - Swipe Refresh Layout: Usado para implementação da atualização da tela ao fazer o swipe na tela.
  - View Model: Utilizado para fornecer um pouco de desacoplamento entre a camada de dados e a view.
  - Navigation: Utilizado para facilitar o código da navegação entre telas no app.
  - Fragment: Os fragments foram utilizados para fornecer uma organização melhor para as telas do meu app. Além de funcionar muito bem com o NvigationComponent.
  - LiveData: Obedece o ciclo de vida da view e fornece a funcionalidade de observer para variáveis que necessitam ser observadas para notificar quando os dados foram obtidos para fazer todo o processo de atualização das views.
  - ViewBinding: Fornece uma maneira simples de referenciar os elementos da view nas classes que precisam manipular de alguma forma esses elementos.
  - Espresso: biblioteca utilizada para fazer testes de ui no meu aplicativo.
  - JUnit4: biblioteca para conseguir rodar testes automatizados no android studio.

- Arquitetura 
  - MVVM (View - ViewModel - Model): Utilizada para colocar um intermediário entre a view e a lógica de negócio. No meu app a viewModel presente na arquitetura mvvm me ajuda a separar a lógica de obtenção de dados da camada de view(fragments e activity).
  - Eu também utilizei uma interface para servir de intermediario entre a api e a classe que deseja obter os dados.
(Fragments -> ViewModel -> Repository -> api)
  
- Bibliotecas 
  - [Retrofit](https://square.github.io/retrofit/): Biblioteca para fazer requisições HTTP para as APIs.
  - [Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson): Biblioteca para converter objetos JSON em objetos JAVA compreendíveis no Android Studio.
  - [okHttp Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor): Biblioteca que serve para obter Logs mais detalhados das requisições HTTPs.
  - [Glide](https://github.com/bumptech/glide): Biblioteca para carregar imagens através da url e armazená-las em cache.
  - [Dagger Hilt](https://dagger.dev/hilt/gradle-setup): Biblioteca usada para injetar a instância do repository na viewModel.
  - [Hamcrest](https://hamcrest.org/JavaHamcrest/index): Biblioteca usada para prover algumas asserções para facilitar meus testes de ui.


## Arquitetura
**App Resultados Copa 2022** utiliza a arquitetura [MVVM]
(https://developer.android.com/topic/architecture).
</br></br>
<img width="60%" src="https://github.com/rubens23/App-Resultados-Copa-2022/raw/main/app/src/main/appscreenshots/Screenshot_arquitetura.PNG">
<br>

## API de terceiros

App Resultados Copa 2022 usa duas apis criadas por mim utilizando github actions. São duas apis: uma para os dados dos jogos e outra para os dados das tabelas. 

## Features

### Ver placar dos jogos e filtrar por grupo, rodada, ou dia atual.
<img alt="screenshot" width="30%" src="https://github.com/rubens23/App-Resultados-Copa-2022/raw/main/app/src/main/appscreenshots/Screenshot_20230605_141215.png"/>


### Ver tabelas dos grupos e filtrar por grupo.
<img alt="screenshot" width="30%" src="https://github.com/rubens23/App-Resultados-Copa-2022/raw/main/app/src/main/appscreenshots/Screenshot_20230605_141302.png"/>

### Suporte para o modo noturno e paisagem.
<img alt="screenshot" width="30%" src="https://github.com/rubens23/App-Resultados-Copa-2022/raw/main/app/src/main/appscreenshots/Screenshot_20230608_131430.png"/>
<img alt="screenshot" width="30%" src="https://github.com/rubens23/App-Resultados-Copa-2022/raw/main/app/src/main/appscreenshots/Screenshot_20230608_131340.png"/>





# Licença



```xml
    Copyright [2023] [Rubens Francisco de Assis]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```









