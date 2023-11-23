const deleteButton = document.getElementById('delete-btn');

if(deleteButton){
    deleteButton.addEventListener('click', event => {
        let id = deleteButton.value;
        fetch(`/img/delete`,{
            method: 'DELETE'
        })
            .then(() => {
                alert(id + '삭제완료');

            }
        }
}

let selectedFiles = [];

function test(files) {
    console.log(files);
    const fileList = document.getElementById('file-list');
    for(let i = 0; i < files.length; i++){
        selectedFiles.push(files[i]);
        const item = document.createElement('div');
        const fileName = document.createTextNode(files[i].name);
        const deleteButton = document.createElement('button');
        deleteButton.addEventListener('click', (event) => {
            item.remove();
            event.preventDefault();
            deleteFile(files[i]);
        });
         deleteButton.innerText="X";
         item.appendChild(fileName);
         item.appendChild(deleteButton);
         fileList.appendChild(item);
        }
    }

function deleteFile(deleteFile){
    const inputFile = document.querySelector('input[name="files"]');
    const dataTransfer = new DataTransfer();
    selectedFiles = selectedFiles.filter(file =>file!==deleteFile);
    selectedFiles.forEach(file => {
        dataTransfer.items.add(file);
    })
    inputFile.files = dataTransfer.files;
    }





