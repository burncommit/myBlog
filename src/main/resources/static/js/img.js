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






