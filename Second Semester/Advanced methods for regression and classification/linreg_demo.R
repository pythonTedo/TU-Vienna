# Motivation:

library(ISLR)
data(Auto)
plot(Auto)
attach(Auto)
plot(mpg~horsepower)
plot(log(mpg)~horsepower)
res <- lm(log(mpg)~horsepower)
abline(res,col=2,lwd=2)
detach(Auto)

res <- lm(log(mpg)~cylinders+displacement+horsepower+weight+acceleration)
summary(res)

#######
data("Wage")
attach(Wage)
plot(wage~age)
lines(smooth.spline(wage~age),col=2,lwd=2)
plot(wage~year)
lines(smooth.spline(wage~year),col=2,lwd=2)
boxplot(wage~education)
detach(Wage)

#######
data("NCI60")
table(NCI60$labs)
res <- prcomp(NCI60$data)
symb <- as.numeric(factor(NCI60$labs))
plot(res$x[,1:2],col=symb,pch=symb)
summary(res)

#######
data("Carseats")
attach(Carseats)
plot(Carseats)
res <- lm(Sales~.,data=Carseats)
summary(res)

train <- sample(nrow(Carseats),300)
res1 <- lm(Sales~CompPrice+Income+Advertising+ShelveLoc,data=Carseats,subset=train)
plot(Sales[train],predict(res1,Carseats[train,]))
plot(Sales[-train],predict(res1,Carseats[-train,]))
abline(c(0,1))


###########
data("College")
res <- lm(Apps~.,data=College)
summary(res)


############################################################################################
# Linear Regression in R

# LS-Regression in R

# Generate data:
set.seed(123)
x <- matrix(runif(60), ncol = 3)
y <- x %*% c(1, 2, 0) + 0.1 * rnorm(20)
colnames(x) <- paste("x", 1:3, sep = "")
d <- data.frame(x, y = y)
plot(d)

# Model with only intercept term
lm0 <- lm(y~1, data = d)
lm0

# Model with intercept plut one explanatory variable:
lm1 <- lm(y~x1, data = d)
lm1

# Full model:
lm3 <- lm(y~x1+x2+x3, data = d)
lm3

# Tests and confidence intervals
summary(lm3)



# Variable selection in R
# Model comparison with anova
anova(lm3)

lm2 <- lm(y~x1+x2, data=d)
anova(lm0, lm1, lm2, lm3)


##############################################################################
# Body fat data:

library("UsingR")
data(fat)
fat <- fat[-c(31,39,42,86), -c(1,3,4,9)]# strange values, not use all variables
attach(fat)

plot(fat, gap=0, cex.labels=.7, cex=0.3,mar=c(.5,.5,.5,.5))


# randomly split into training and test data:
set.seed(1234)
n <- nrow(fat)
train <- sample(1:n,round(n*2/3))
test <- (1:n)[-train]

# Full linear model:
model.lm <- lm(body.fat~., data = fat, subset=train)
summary(model.lm)

pred.lm <- predict(model.lm,newdata = fat[test,])
#  RMSE
sqrt(mean((fat$body.fat[test]-pred.lm))^2)

plot(fat[test,"body.fat"],pred.lm,xlab="y measured",ylab="y predicted",cex.lab=1.3)
abline(c(0,1))


########################################################################################
# Stepwise variable selection:
# 1-step selection with drop1
drop1(model.lm, test="F")
summary(update(model.lm,.~.-weight))

# Comparison of the models with anova
model.lm1 <- (update(model.lm,.~.-weight))

anova(model.lm1,model.lm)


# Automatic model selection with step
# backward selection:
mod.back <- step(model.lm,direction="backward")
summary(mod.back)
# forward selection
mod.empty <- lm(body.fat~1,data=fat, subset=train)
mod.forw <- step(mod.empty,scope=formula(model.lm),direction="forward")
summary(mod.forw)

AIC(mod.back)
AIC(mod.forw)

pred.back <- predict(mod.back,fat[test,])
pred.forw <- predict(mod.forw,fat[test,])
#  RMSE
sqrt(mean((fat$body.fat[test]-pred.back))^2)
sqrt(mean((fat$body.fat[test]-pred.forw))^2)




########################################################################################
# Best-Subset regression with Leaps-and-Bound algorithm
library(leaps)
lm.regsubset <- regsubsets(body.fat~., data=fat, nbest = 1, subset=train)
summary(lm.regsubset)

plot(lm.regsubset)

str(summary(lm.regsubset))
BIC <- summary(lm.regsubset)$bic
plot(BIC)


modregsubset.lm <- lm(body.fat~height+wrist+abdomen,data=fat,subset=train)
pred.regsubset <- predict(modregsubset.lm,newdata = fat[test,])
#  RMSE
sqrt(mean((fat$body.fat[test]-pred.regsubset))^2)



###############################################################
# Derived inputs as regressors in R

# The problem of correlated regressors

set.seed(123)
x1 <- runif(100)
y <- x1 + 0.5 * rnorm(100)
plot(x1,y)
summary(lm(y ~ x1))

x2 <- runif(100)
plot(data.frame(y,x1,x2))
summary(lm(y ~ x1 + x2))

x3 <- x1 + 0.1 * runif(100)
plot(data.frame(y,x1,x2,x3))
res <- lm(y ~ x1 + x3)
summary(res)

res <- lm(y ~ x1 + x2+ x3)
summary(res)


alias(lm(y~ x1+x3)) # linear dependencies

X <- model.matrix(res)
t(X)%*%X
solve(t(X)%*%X)


fat.mod <- fat
fat.mod$nonsense <- fat$neck+2*fat$chest-3*fat$abdomen
alias(lm(body.fat~., data=fat.mod))



########################################################################################
# Principal Component Regression
library(pls)
model.pcr <- pcr(body.fat~., data=fat, scale=TRUE, subset=train,
                 validation="CV", segments=10, segment.type="random")
summary(model.pcr)
plot(model.pcr, plottype = "validation", val.type = "RMSEP", legend = "topright")

plot(model.pcr, ncomp=12, asp=1, line=TRUE)

pred.pcr <- predict(model.pcr,newdata=fat[test,],ncomp=12)
#  RMSE
sqrt(mean((fat$body.fat[test]-pred.pcr)^2))


########################################################################################
# PLS Regression
model.pls <- plsr(body.fat~., data=fat, scale=TRUE, subset=train,
                 validation="CV", segments=10, segment.type="random")
summary(model.pls)
plot(model.pls, plottype = "validation", val.type = "RMSEP", legend = "topright")

plot(model.pls, ncomp=7, asp=1, line=TRUE)

pred.pls <- predict(model.pls,newdata=fat[test,],ncomp=7)
#  RMSE
sqrt(mean((fat$body.fat[test]-pred.pls)^2))


########################################################################################
# Shrinkage methods in R

########################################################################################
# Ridge Regression

library(MASS)
model.ridge <- lm.ridge(body.fat~., data=fat, lambda=seq(0,15, by=0.2), subset=train)
plot(model.ridge$lambda,model.ridge$GCV,type="l")

select(model.ridge)

lambda.opt <- model.ridge$lambda[which.min(model.ridge$GCV)]

plot(0,0,xlim=range(model.ridge$lambda),ylim=range(model.ridge$coef),
     type="n",xlab="lambda",ylab="beta")
for(i in 1:nrow(model.ridge$coef))
{
   lines(model.ridge$lambda,model.ridge$coef[i,],col=i)
}
legend("topright", legend=rownames(model.ridge$coef), lty=rep(1,14),col=1:14, cex=0.65)
abline(v=lambda.opt, lty=2)


# Prediction with Ridge:
mod.ridge <- lm.ridge(body.fat~., data=fat, lambda = lambda.opt, subset=train)
mod.ridge$coef # coefficients for scaled x

ridge.coef <- coef(mod.ridge)
ridge.coef # coefficients in original scale + intercept

pred.ridge <- as.matrix(cbind(rep(1,length(test)),fat[test,-1]))%*%ridge.coef
plot(fat[test,"body.fat"],pred.ridge)
abline(c(0,1))
#  RMSE
sqrt(mean((fat$body.fat[test]-pred.ridge))^2)



########################################################################################
# Lasso regression with glmnet
library(glmnet)
res <- glmnet(as.matrix(fat[train,-1]),fat[train,1])
print(res)
plot(res)
coef(res)
res.cv <- cv.glmnet(as.matrix(fat[train,-1]),fat[train,1])
plot(res.cv)
coef(res.cv,s="lambda.1se")

pred.lasso <- predict(res.cv,newx=as.matrix(fat[test,-1]),s="lambda.1se")
#  RMSE
sqrt(mean((fat$body.fat[test]-pred.lasso))^2)


plot(fat[test,"body.fat"],pred.lasso)
abline(c(0,1))

