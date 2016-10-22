using Microsoft.WindowsAzure.Mobile.Service;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace transporterService.DataTranspObjects
{
    public partial class StatusDescriptionItemDTO : EntityData
    {
        public string Name { get; set; }
        public int Type { get; set;  }
        public int UserType { get; set; }

        //UserType: 0 = none, 1 = transporta, 2 = envia
    }

    public static class StatusDescriptionType
    {
        public static string GetDescription(int type)
        {
            switch (type)
            {
                case 1:
                    return "Envio agendado";
                case 2:
                    return "Aguardando retirada do produto";
                case 3:
                    return "Produto retirado";
                case 4:
                    return "Transporte Iniciado";
                case 5:
                    return "Chegou no destino";
                case 6:
                    return "Produto entregue no correio";
                case 7:
                    return "Produto entregue no destinatário";
                case 8:
                    return "Aguardando qualificação do Transportador";
                case 9:
                    return "Aguardando a sua qualificação"; //Cliente
                case 98:
                    return "Finalizado";
                case 99:
                    return "Ocorreu um problema";
                default:
                    return "";
            }
        }

    }
}