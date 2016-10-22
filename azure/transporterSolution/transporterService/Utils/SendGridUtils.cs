using SendGrid;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Web;

namespace transporterService.Utils
{
    public class SendGridUtils
    {
        public const string updateStatusSubject = "Dover - {0}";
        public const string updateStatusMessage = "Dover - Atualização de status\n"
            + "O seu produto que está sendo enviado por {0}\n"
            + "recebeu uma atualização de status para: '{1}'\n"
            + "Qualquer dúvida, entre em contato pelo email {2} para obter maiores detalhes";

        public const string inviteSubject = "Você recebeu um convite para testar o Dover";
        public const string inviteMessage = "Dover é um aplicativo colaborativo para envio de encomendas e está em fase de protótipo, validação e testes.\n"
            + "Você foi convidado {0} para acessar a aplicação e seu código de acesso é: {1}\n\n"
            + "Baixe o aplicativo através da url: https://dover.blob.core.windows.net/public/app-dover.apk" + ", apenas lembrando que funciona apenas a partir da versão 5.0 do Android!\n\n"
            + "Por favor, acesse o aplicativo para Android e selecione a opção 'Registrar Agora' e utilize seu email e o código fornecido para se registrar. \n\n"
            + "Após se registrar, realize o login e então nos fale como foi sua experiência de uso. Sua opinião é muito importartante para nós\n"
            + "Após instalar e acessar a aplicação, responda um questionário com apenas 6 perguntas no endereço: " 
            + "http://goo.gl/forms/hgThHSLMCo"
            + " \n\n"
            + "\n\n\nAtenciosamente,\nPaulo Figueiredo e Fellipe Ugliara";

        public class EmailUser
        {
            public class Attachment {
                public Attachment()
                {
                    StreamContent = new MemoryStream();
                }
                public MemoryStream StreamContent { get; set; }
                public string Name { get; set; }
            }

            public string toEmail { get; set; }
            public string subject { get; set; }

            public string fromEmail { get; set; }
            public string fromName { get; set;  }

            public string content { get; set; }
            public List<Attachment> Attachments { get; set;  }

            public EmailUser(string email, string subject, string content)
            {
                fromName = "Paulo Cruz";
                fromEmail = "pacefico@gmail.com";
                this.toEmail = email;
                this.subject = subject;
                this.content = content;
                this.Attachments = new List<Attachment>();
            }
            
        }

        private NetworkCredential credentials;

        public SendGridUtils()
        {
            var username = "azure_dbff43d4181a7414504811c532368d08@azure.com";
            var pswd = "ARiQi61Gf6lMPN7";

            credentials = new NetworkCredential(username, pswd);
        }

        public void send(EmailUser user)
        {
            // Create the email object first, then add the properties.
            SendGridMessage myMessage = new SendGridMessage();
            myMessage.AddTo(user.toEmail);
            myMessage.From = new MailAddress(user.fromEmail, user.fromName);
            myMessage.Subject = user.subject;
            myMessage.Text = user.content;

            string photoName = "foto";
            int number = 1;
            
            foreach (var item in user.Attachments)
            {
                if (item != null)
                {
                    string name = photoName + Convert.ToString(number) + ".jpg";
                    myMessage.AddAttachment(item.StreamContent, name); 
                    number++;
                }
            }
            
            // Create credentials, specifying your user name and password.
            //var credentials = new NetworkCredential("username", "password");

            // Create an Web transport for sending email.
            var transportWeb = new Web(this.credentials);

            // Send the email.
            // You can also use the **DeliverAsync** method, which returns an awaitable task.
            transportWeb.DeliverAsync(myMessage);
        }

    }
}