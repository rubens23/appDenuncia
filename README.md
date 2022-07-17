# appDenuncia
appDenuncia.

A ideia desse app partiu de um projeto que eu tive na faculdade. O escopo do projeto seria fazer duas aplicações, uma para desktop e outra para mobile.
A aplicação para mobile focaria nas funcionalidades destinadas aos usuários que poderiam realizar denúncias de problemas que eles notaram em seu bairro ou cidade.

O app conta com telas de login para autenticação dos usuários, uma tela de menu para facilitar a navegação dentro do aplicativo, uma tela para registro de novas
denúncias, uma tela para visualizar as denúncias que você já fez e uma tela para visualizar as denúncias de outras pessoas que também usam o aplicativo.

A ideia desse aplicativo é ser conectado com outra aplicação que será destinada a profissionais que administrarão e darão tratamento a essas denúncias.

A seguir segue print das telas do app junto com uma breve explicação de como ele foi feito:


![Screenshot_20220717135206](https://user-images.githubusercontent.com/104860234/179416232-ad2f593f-2c11-4f2c-ba3f-29b0d61f6bbd.jpg)

Essa é a tela de login. O código de autenticação foi feito com o auxílio da ferramenta de autenticação do firebase.

![Screenshot_20220717135310](https://user-images.githubusercontent.com/104860234/179416263-bda9e589-5579-4dfe-81d8-aaced5a05f71.jpg)

Essa é a tela de esqueci senha. O usuário informa o email e se o email for encontrado, um email é enviado para ele alterar a senha.

![Screenshot_20220717135510](https://user-images.githubusercontent.com/104860234/179416328-0967de8b-93af-4743-a357-c79dcfccca55.jpg)

Essa é a tela de cadastro, feita utilizando métodos de autenticação do firebase para criar um email e senha válidos.

![Screenshot_20220717135735](https://user-images.githubusercontent.com/104860234/179416418-b88eb499-0a99-4e3d-8dd6-bf8fb4d5bb79.jpg)

Essa é a tela de menu, com botões que direcionarão o user para telas úteis para registrar denúncias e acompanhar seus status de resolução
e, também, denúncias de outras pessoas que usam o app.

![Screenshot_20220717135947](https://user-images.githubusercontent.com/104860234/179416542-3137b256-5e07-4bb3-a837-73068a162747.jpg)

Acima é a tela para registro de novas denúncias. O spinner serve para que o usuário defina um tipo para a reclamação e depois escreva
um texto explicando sobre sua reclamação. Todas essas informações são captadas e armazenadas em um banco de dados SQLite.

![Screenshot_20220717140238](https://user-images.githubusercontent.com/104860234/179416666-ca1a1fe4-c183-4807-be32-ae59f6d0ed3e.jpg)

Clicando no segundo botão, o usuário chega nessa tela. É uma tela para o usuário acompanhar as reclamações que ele já enviou, o status
dessa reclamação, comentários de outros usuários sobre essa reclamação e ele também pode adicionar uma foto que represente a sua 
reclamação.

![Screenshot_20220717140615](https://user-images.githubusercontent.com/104860234/179416781-a2c7b550-13c3-473f-9fa7-c4398a4843fa.jpg)

E por fim, uma tela para o usuário acompanhar a reclamação de todo mundo que usa o app. Podendo até mesmo adicionar um like para as
reclamações que ele acha relevantes.











