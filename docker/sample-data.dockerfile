FROM python:3.9-bullseye

WORKDIR /app

COPY requirements.txt .

RUN pip install -r requirements.txt

COPY sample_data.py .

CMD ["./sample_data.py", "./data/samples"]
# ENTRYPOINT [ "/bin/bash" ]
