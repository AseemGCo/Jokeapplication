# Deployment Guide - AI Joke Generator

This guide will help you deploy your Spring Boot joke application to various cloud platforms.

## Prerequisites

1. **GitHub Account**: You'll need a GitHub account to host your code
2. **Java 17+**: Ensure you have Java 17 or higher installed locally
3. **Maven**: Make sure Maven is installed and working

## Option 1: Railway (Recommended - Easiest)

### Step 1: Push to GitHub
```bash
# Initialize git if not already done
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
git push -u origin main
```

### Step 2: Deploy on Railway
1. Go to [railway.app](https://railway.app)
2. Sign up with your GitHub account
3. Click "New Project" → "Deploy from GitHub repo"
4. Select your repository
5. Railway will automatically detect it's a Java app and deploy it
6. Your app will be available at the provided URL

**Pros**: 
- Very easy deployment
- Automatic HTTPS
- Good free tier
- Auto-deploys on git push

## Option 2: Render

### Step 1: Push to GitHub (same as above)

### Step 2: Deploy on Render
1. Go to [render.com](https://render.com)
2. Sign up with your GitHub account
3. Click "New" → "Web Service"
4. Connect your GitHub repository
5. Configure:
   - **Name**: joke-generator
   - **Environment**: Java
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/joke-generator-1.0.0.jar`
6. Click "Create Web Service"

**Pros**: 
- Good free tier
- Easy deployment
- Automatic HTTPS

**Cons**: 
- Sleeps after 15 minutes of inactivity

## Option 3: Heroku

### Step 1: Install Heroku CLI
Download from [heroku.com](https://devcenter.heroku.com/articles/heroku-cli)

### Step 2: Deploy
```bash
# Login to Heroku
heroku login

# Create Heroku app
heroku create your-joke-app-name

# Set Java version
heroku config:set JAVA_VERSION=17

# Deploy
git push heroku main

# Open the app
heroku open
```

**Pros**: 
- Excellent Spring Boot support
- Great documentation
- Reliable

**Cons**: 
- No free tier anymore ($5/month minimum)

## Option 4: Google Cloud Platform (GCP)

### Step 1: Setup GCP
1. Go to [console.cloud.google.com](https://console.cloud.google.com)
2. Create a new project
3. Enable Cloud Run API

### Step 2: Deploy with Cloud Run
```bash
# Install Google Cloud CLI
# Download from: https://cloud.google.com/sdk/docs/install

# Login
gcloud auth login

# Set project
gcloud config set project YOUR_PROJECT_ID

# Build and deploy
gcloud run deploy joke-generator \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated
```

**Pros**: 
- Very reliable
- Good performance
- Generous free tier

## Option 5: AWS Elastic Beanstalk

### Step 1: Setup AWS
1. Create AWS account
2. Install AWS CLI
3. Configure AWS credentials

### Step 2: Deploy
```bash
# Install EB CLI
pip install awsebcli

# Initialize EB application
eb init

# Create environment
eb create joke-generator-env

# Deploy
eb deploy
```

**Pros**: 
- Most comprehensive
- Very reliable
- Good for scaling

**Cons**: 
- More complex setup
- Limited free tier

## Environment Variables

For all platforms, you can set these environment variables:

- `PORT`: The port your app runs on (usually set automatically)
- `JAVA_OPTS`: JVM options (e.g., `-Xmx512m -Xms256m`)

## Troubleshooting

### Common Issues:

1. **Port Issues**: Make sure your app uses `server.port=${PORT:8080}` in `application.properties`

2. **Memory Issues**: Set `JAVA_OPTS=-Xmx512m -Xms256m` for memory-constrained environments

3. **Build Failures**: Ensure all dependencies are in `pom.xml`

4. **WebSocket Issues**: Some platforms may require additional configuration for WebSocket support

### Testing Locally Before Deployment:

```bash
# Build the app
mvn clean package

# Test with different port
PORT=8081 java -jar target/joke-generator-1.0.0.jar
```

## Monitoring

After deployment, monitor your app:

1. **Health Check**: Visit `/actuator/health`
2. **Application Info**: Visit `/actuator/info`
3. **Metrics**: Visit `/actuator/metrics`

## Cost Optimization

- **Railway**: $5/month credit (usually sufficient for small apps)
- **Render**: Free tier with 750 hours/month
- **GCP**: $300 credit for 90 days, then limited free tier
- **AWS**: 12 months free, then pay-as-you-go

## Recommended for Beginners

**Start with Railway** - it's the easiest to set up and has a good free tier for learning and small projects.

## Next Steps

After deployment:
1. Test all features work correctly
2. Set up monitoring
3. Configure custom domain (optional)
4. Set up CI/CD for automatic deployments

---

**Happy Deploying! 🚀**
