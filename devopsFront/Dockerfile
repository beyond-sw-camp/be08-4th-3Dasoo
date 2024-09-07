FROM nginx:stable-alpine3.20
LABEL maintainer="uzz99 <yj.lee991228@gmail.com>"
COPY dist /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf
ENV TZ=Asia/Seoul
EXPOSE 80
# nginx를 실행하는 명령어 작성
CMD ["nginx","-g","daemon off;"]