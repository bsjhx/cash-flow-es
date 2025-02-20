#!/bin/sh

DOCKER_IMAGE_NAME="cash-flow:v1.0"
SUBSCRIPTION_ID="find-on-azure"
RESOURCE_GROUP_NAME="resource-group-name"
REGISTRY_NAME="registryname"
ENV_NAME="cash-flow-dev-env"
APP_NAME="cash-flow-backend"
APP_PORT=8080

# 1. Install Azure CLI & Login
brew install azure-cli

az login

# Azure Portal -> Subscriptions
az account set --subscription $SUBSCRIPTION_ID

# 2. Create a Resource Group
az group create --name $RESOURCE_GROUP_NAME --location westeurope

# 3. Create an Azure Container Registry (ACR)
az acr create --name $REGISTRY_NAME --resource-group $RESOURCE_GROUP_NAME --sku Basic

az acr login --name $REGISTRY_NAME

# 4. Build & Push Your Docker Image
docker build --platform linux/amd64 -t $REGISTRY_NAME.azurecr.io/$DOCKER_IMAGE_NAME .
docker push $REGISTRY_NAME.azurecr.io/$DOCKER_IMAGE_NAME

# 5. Create environment

az containerapp env create \
  --name $ENV_NAME \
  --resource-group $RESOURCE_GROUP_NAME \
  --location westeurope

# 6. Deploy app
az containerapp create \
--name $APP_NAME \
--resource-group $RESOURCE_GROUP_NAME \
--image $REGISTRY_NAME.azurecr.io/$DOCKER_IMAGE_NAME \
--environment $ENV_NAME \
--target-port $APP_PORT \
--ingress external \
--registry-server $REGISTRY_NAME.azurecr.io \
--query properties.configuration.ingress.fqdn