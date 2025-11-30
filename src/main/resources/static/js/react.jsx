const App = () => {
	const fruits = [
		{
			name: "apple", 
			description: "水分が多くおいしい果物", 
		}, 
		{
			name: "strawberry", 
			description: "甘い果物", 
		}
	]
	
	return (
		<>
			{fruits.map((fruit) => {fruit})};
		</>
	)
}

export default App;